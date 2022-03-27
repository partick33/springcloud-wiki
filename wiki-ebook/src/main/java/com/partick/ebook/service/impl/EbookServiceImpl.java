package com.partick.ebook.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.partick.common.exception.BusinessException;
import com.partick.common.req.EbookQueryReq;
import com.partick.common.req.EbookSaveReq;
import com.partick.common.resp.EbookQueryResp;
import com.partick.common.resp.PageResp;
import com.partick.common.utils.CopyUtil;
import com.partick.common.utils.SnowFlake;
import com.partick.database.entity.Ebook;
import com.partick.database.entity.EbookExample;
import com.partick.database.mapper.EbookMapper;
import com.partick.ebook.feign.EbookSnapshotClient;
import com.partick.ebook.service.EbookService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class EbookServiceImpl implements EbookService {
    @Autowired
    EbookMapper ebookMapper;

    @Autowired
    SnowFlake snowFlake;

    @Autowired
    EbookSnapshotClient ebookSnapshotClient;

    @Override
    public PageResp<EbookQueryResp> selectByExample(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if (!ObjectUtils.isEmpty(req.getCategoryId2())) {
            criteria.andCategory2IdEqualTo(req.getCategoryId2());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        List<EbookQueryResp> ebookQueryRespList = CopyUtil.copy(ebookList, EbookQueryResp.class);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        PageResp<EbookQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(ebookQueryRespList);
        return pageResp;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        String logId = MDC.get("LOG_ID");
        if (ObjectUtils.isEmpty(req.getId())) {
            try {
                //新增
                ebook.setId(snowFlake.nextId());
                ebook.setDocCount(0);
                ebook.setViewCount(0);
                ebook.setVoteCount(0);
                ebookMapper.insert(ebook);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
            ebookSnapshotClient.insertProducer(req.getName(), logId);
        } else {
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }

    }
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void delete(Long id) {
        Ebook ebook = ebookMapper.selectByPrimaryKey(id);
        String logId = MDC.get("LOG_ID");
        if (!ObjectUtils.isEmpty(ebook)) {
            try {
                ebookMapper.deleteByPrimaryKey(id);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
            ebookSnapshotClient.deleteProducer(id, ebook.getName(), logId);
            System.out.println(1);
        }
    }
}
