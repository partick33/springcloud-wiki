package com.partick.category.controller;


import com.partick.category.service.CategoryService;
import com.partick.common.req.CategoryQueryReq;
import com.partick.common.req.CategorySaveReq;
import com.partick.common.resp.CommonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq req){
        return new CommonResp(true, "", categoryService.selectByExample(req));
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid CategorySaveReq req){
        categoryService.save(req);
        return new CommonResp(true, "");
    }

    @PostMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        categoryService.delete(id);
        return new CommonResp(true, "");
    }

    @GetMapping("/all")
    public CommonResp all(){
        return new CommonResp(true, "", categoryService.all());
    }
}
