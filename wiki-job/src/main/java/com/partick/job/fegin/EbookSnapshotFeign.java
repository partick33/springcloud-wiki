package com.partick.job.fegin;

import com.partick.common.config.FeignConfiguration;
import com.partick.common.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "wiki-ebook-snapshot",configuration = FeignConfiguration.class)
public interface EbookSnapshotFeign {

    @GetMapping("/ebook-snapshot/insertEbookSnapshot")
    public CommonResp insertEbookSnapshot();

    @GetMapping("/ebook-snapshot/updateEbookSnapshot")
    public CommonResp updateEbookSnapshot();

    @GetMapping("/ebook-snapshot/updateEbookSnapshotIncrease")
    public CommonResp updateEbookSnapshotIncrease();
}