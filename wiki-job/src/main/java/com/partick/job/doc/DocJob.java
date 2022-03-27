package com.partick.job.doc;

import com.partick.common.utils.SnowFlake;
import com.partick.job.fegin.DocFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DocJob {
    @Autowired
    private DocFeign docFeign;
    @Autowired
    private SnowFlake snowFlake;
    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Scheduled(cron = "0 0/30 * * * ?")
    public void cron() throws InterruptedException{
        //增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("更新电子书下的文档数据开始");
        long start = System.currentTimeMillis();
        docFeign.updateEbookInfo();
        LOG.info("更新电子书下的文档数据开始,耗时：{}毫秒", System.currentTimeMillis() - start);
    }
}
