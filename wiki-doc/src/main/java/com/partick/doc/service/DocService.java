package com.partick.doc.service;


import com.partick.common.req.DocQueryReq;
import com.partick.common.req.DocSaveReq;
import com.partick.common.resp.DocQueryResp;

import java.util.List;

public interface DocService {

    List<DocQueryResp> selectByExample(DocQueryReq req);

    public void save(DocSaveReq req);

    public void delete(Long id);

    void delete(List<String> ids);

    List<DocQueryResp> all(Long ebookId);

    String findContent(Long id);

    int vote(Long id);

    void updateEbookInfo();
}
