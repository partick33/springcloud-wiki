package com.partick.job.ebooksnapshot;

import com.partick.common.resp.CommonResp;
import com.partick.common.utils.SnowFlake;
import com.partick.job.fegin.EbookSnapshotFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EbookSnapshotJob {
    @Autowired
    EbookSnapshotFeign ebookSnapshotFeign;
    @Autowired
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotJob.class);


    /**
     *每天每58分钟执行一次
     */
    @Scheduled(cron = "0 0/58 * * * ?")
    public void insertEbookSnapshot() {
        //增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("新增今天电子书快照记录开始");
        long start = System.currentTimeMillis();
        CommonResp commonResp = ebookSnapshotFeign.insertEbookSnapshot();
        int rows = (int) commonResp.getContent();
        LOG.info("新增今天电子书快照记录,受影响行数：{}行,耗时：{}毫秒", rows, System.currentTimeMillis() - start);
    }

    /**
     * 每日每30分钟更新一次
     */
    @Scheduled(cron = "0 0/30 * * * ? ")
    public void insertOrUpdateSnapshotCorn() {
        //增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("更新电子书快照开始");
        long start = System.currentTimeMillis();
        CommonResp commonResp = ebookSnapshotFeign.updateEbookSnapshot();
        int rows = (int) commonResp.getContent();
        LOG.info("更新电子书快照结束,受影响行数：{}行,耗时：{}毫秒", rows, System.currentTimeMillis() - start);
    }

    /**
     * 每日每小时更新一次
     */
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void updateEbookSnapshotIncreaseCorn() {
        //增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("更新电子书快照新增数据开始");
        long start = System.currentTimeMillis();
        CommonResp commonResp = ebookSnapshotFeign.updateEbookSnapshotIncrease();
        int rows = (int) commonResp.getContent();
        LOG.info("更新电子书快照新增数据结束,受影响行数：{}行,耗时：{}毫秒", rows, System.currentTimeMillis() - start);
    }
}
