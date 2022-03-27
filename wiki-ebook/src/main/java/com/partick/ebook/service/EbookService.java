package com.partick.ebook.service;


import com.partick.common.req.EbookQueryReq;
import com.partick.common.req.EbookSaveReq;
import com.partick.common.resp.EbookQueryResp;
import com.partick.common.resp.PageResp;

public interface EbookService {
    //查询书本列表
    PageResp<EbookQueryResp> selectByExample(EbookQueryReq req);

    //保存书本
    public void save(EbookSaveReq req);

    //删除书本
    public void delete(Long id);
}
