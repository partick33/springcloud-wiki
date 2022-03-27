package com.partick.category.service.impl;


import com.partick.category.service.CategoryService;
import com.partick.common.req.CategoryQueryReq;
import com.partick.common.req.CategorySaveReq;
import com.partick.common.resp.CategoryQueryResp;
import com.partick.common.utils.CopyUtil;
import com.partick.common.utils.SnowFlake;
import com.partick.common.utils.SpecialCharactersUntil;
import com.partick.database.entity.Category;
import com.partick.database.entity.CategoryExample;
import com.partick.database.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    SnowFlake snowFlake;

    @Override
    public List<CategoryQueryResp> selectByExample(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        List<Category> categories = new ArrayList<>();
        if (!ObjectUtils.isEmpty(req.getName())) {
            //方法一：分开执行两次查询
            /*criteria.andNameLike("%" + req.getName() + "%");
            List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
            Map map = new HashMap();
            //取出父节点的集合
            for (int i = 0; i < categoryList.size(); i++) {
                Category category = categoryList.get(i);
                categories.add(category);
                map.put(category.getParent(), category.getParent());
            }
            //使用迭代器实现Map循环取值
            for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                CategoryExample categoryExample1 = new CategoryExample();
                CategoryExample.Criteria criteria1 = categoryExample1.createCriteria();
                criteria1.andIdEqualTo((Long) entry.getValue());
                List<Category> categoryList1 = categoryMapper.selectByExample(categoryExample1);
                for (int i = 0; i < categoryList1.size(); i++) {
                    Category category =  categoryList1.get(i);
                    categories.add(category);
                }
            }*/
            //方法二：sql Union查询
            String name = SpecialCharactersUntil.SpecialCharactersHandler(req.getName());
            List<Category> categoryList = categoryMapper.selectByName(name);
            for (int i = 0; i < categoryList.size(); i++) {
                Category category =  categoryList.get(i);
                categories.add(category);
            }
        }else {
            categoryExample.setOrderByClause("sort asc");
            List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
            for (int i = 0; i < categoryList.size(); i++) {
                Category category =  categoryList.get(i);
                categories.add(category);
            }
        }
        return CopyUtil.copy(categories, CategoryQueryResp.class);
    }

    @Override
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
            //更新
            categoryMapper.updateByPrimaryKey(category);
        }

    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<CategoryQueryResp> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        return CopyUtil.copy(categoryList, CategoryQueryResp.class);
    }
}
