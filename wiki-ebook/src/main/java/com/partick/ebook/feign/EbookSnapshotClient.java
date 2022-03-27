package com.partick.ebook.feign;

import com.partick.common.config.FeignConfiguration;
import com.partick.common.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "wiki-ebook-snapshot",configuration = FeignConfiguration.class)
public interface EbookSnapshotClient {

    @PostMapping("/ebook-snapshot/insertProducer")
    CommonResp insertProducer(@RequestParam("ebookName") String ebookName, @RequestParam("logId") String logId);

    @PostMapping("/ebook-snapshot/deleteProducer")
    CommonResp deleteProducer(@RequestParam("ebookId") Long ebookId,@RequestParam("ebookName") String ebookName, @RequestParam("logId") String logId);
}
