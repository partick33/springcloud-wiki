package com.partick.doc.controller;


import com.partick.common.req.DocQueryReq;
import com.partick.common.req.DocSaveReq;
import com.partick.common.resp.CommonResp;
import com.partick.doc.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    DocService docService;

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req) {
        return new CommonResp(true, "", docService.selectByExample(req));
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid DocSaveReq req) {
        docService.save(req);
        return new CommonResp(true, "");
    }

    @PostMapping("/delete/{idStr}")
    public CommonResp delete(@PathVariable String idStr) {
        List<String> list = Arrays.asList(idStr.split(","));
        docService.delete(list);
        return new CommonResp(true, "");
    }

    @GetMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId) {
        return new CommonResp(true, "", docService.all(ebookId));
    }

    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id) {
        return new CommonResp(true, "", docService.findContent(id));
    }

    @PostMapping("/vote/{id}")
    public CommonResp vote(@PathVariable Long id) {
        return new CommonResp(true, "", docService.vote(id));
    }

    @GetMapping("/updateEbookInfo")
    public CommonResp updateEbookInfo() {
        docService.updateEbookInfo();
        return new CommonResp(true, "");
    }
}
