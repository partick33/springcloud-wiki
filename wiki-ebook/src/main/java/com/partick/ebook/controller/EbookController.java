package com.partick.ebook.controller;


import com.partick.common.req.EbookQueryReq;
import com.partick.common.req.EbookSaveReq;
import com.partick.common.resp.CommonResp;
import com.partick.ebook.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/ebook")
public class EbookController {
    @Autowired
    EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(@Valid EbookQueryReq req){
        return new CommonResp(true, "", ebookService.selectByExample(req));
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid EbookSaveReq req){
        ebookService.save(req);
        return new CommonResp(true, "");
    }

    @PostMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        ebookService.delete(id);
        return new CommonResp(true, "");
    }
}
