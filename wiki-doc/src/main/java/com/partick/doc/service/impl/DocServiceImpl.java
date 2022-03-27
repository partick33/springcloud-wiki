package com.partick.doc.service.impl;


import com.partick.common.exception.BusinessException;
import com.partick.common.exception.BusinessExceptionCode;
import com.partick.common.req.DocQueryReq;
import com.partick.common.req.DocSaveReq;
import com.partick.common.resp.DocQueryResp;
import com.partick.common.utils.CopyUtil;
import com.partick.common.utils.RedisUntil;
import com.partick.common.utils.RequestContext;
import com.partick.common.utils.SnowFlake;
import com.partick.database.entity.Content;
import com.partick.database.entity.Doc;
import com.partick.database.entity.DocExample;
import com.partick.database.mapper.ContentMapper;
import com.partick.database.mapper.DocMapper;
import com.partick.database.mapper.DocMapperCust;
import com.partick.doc.rabbitMQ.VoteMessageProducer;
import com.partick.doc.service.DocService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class DocServiceImpl implements DocService {
    @Autowired
    DocMapper docMapper;

    @Autowired
    SnowFlake snowFlake;

    @Autowired
    ContentMapper contentMapper;

    @Autowired
    DocMapperCust docMapperCust;

    @Autowired
    RedisUntil redisUntil;

    @Autowired
    VoteMessageProducer voteMessageProducer;

    @Override
    public List<DocQueryResp> selectByExample(DocQueryReq req) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        List<Doc> docs = new ArrayList<>();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
            List<Doc> docList = docMapper.selectByExample(docExample);
            Map map = new HashMap();
            //取出父节点的集合
            for (int i = 0; i < docList.size(); i++) {
                Doc doc = docList.get(i);
                docs.add(doc);
                map.put(doc.getParent(), doc.getParent());
            }
            //使用迭代器实现Map循环取值
            for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                DocExample docExample1 = new DocExample();
                DocExample.Criteria criteria1 = docExample1.createCriteria();
                criteria1.andIdEqualTo((Long) entry.getValue());
                List<Doc> docList1 = docMapper.selectByExample(docExample1);
                for (int i = 0; i < docList1.size(); i++) {
                    Doc doc = docList1.get(i);
                    docs.add(doc);
                }
            }
            docExample.setOrderByClause("sort asc");
            List<Doc> list = docMapper.selectByExample(docExample);
            for (int i = 0; i < list.size(); i++) {
                Doc doc = docList.get(i);
                docs.add(doc);
            }
        }
        return CopyUtil.copy(docs, DocQueryResp.class);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            //更新
            docMapper.updateByPrimaryKey(doc);
            if (ObjectUtils.isEmpty(content.getId())) {
                contentMapper.insert(content);
            }
            contentMapper.updateByPrimaryKeyWithBLOBs(content);
        }
    }

    @Override
    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    @Override
    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        return CopyUtil.copy(docList, DocQueryResp.class);
    }

    @Override
    public String findContent(Long id) {
        Content contents = contentMapper.selectByPrimaryKey(id);
        //文档阅读数加一
        docMapperCust.increaseViewCount(id);
        if (ObjectUtils.isEmpty(contents)) {
            return "";
        } else {
            return contents.getContent();
        }
    }

    @Override
    public int vote(Long id) {
        //远程ip+doc.id作为key，24小时内不能重复
        String ip = RequestContext.getRemoteAddr();
        if (redisUntil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600 * 24)) {
            docMapperCust.increaseVoteCount(id);
            Doc doc = docMapper.selectByPrimaryKey(id);
            voteMessageProducer.voteMessageSend(doc.getName(), MDC.get("LOG_ID"));
            return doc.getVoteCount();
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
    }

    @Override
    public void updateEbookInfo() {
        docMapperCust.updateEbookInfo();
    }
}
