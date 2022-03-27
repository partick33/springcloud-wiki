package com.partick.job.fegin;

import com.partick.common.config.FeignConfiguration;
import com.partick.common.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "wiki-doc",configuration = FeignConfiguration.class)
public interface DocFeign {

    @GetMapping("/doc/updateEbookInfo")
    public CommonResp updateEbookInfo();
}