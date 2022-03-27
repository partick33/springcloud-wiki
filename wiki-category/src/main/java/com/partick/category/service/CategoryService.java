package com.partick.category.service;


import com.partick.common.req.CategoryQueryReq;
import com.partick.common.req.CategorySaveReq;
import com.partick.common.resp.CategoryQueryResp;

import java.util.List;

public interface CategoryService {
    //查询书本列表
    List<CategoryQueryResp> selectByExample(CategoryQueryReq req);

    //保存书本
    public void save(CategorySaveReq req);

    //删除书本
    public void delete(Long id);

    List<CategoryQueryResp> all();
}
