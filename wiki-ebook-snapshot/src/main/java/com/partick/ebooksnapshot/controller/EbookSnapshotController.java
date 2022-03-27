package com.partick.ebooksnapshot.controller;


import com.partick.common.resp.CommonResp;
import com.partick.ebooksnapshot.elasticsearch.EbookSnapshotEs;
import com.partick.ebooksnapshot.rabbitMQ.EbookSnapshotMessageProducer;
import com.partick.ebooksnapshot.service.EbookSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {
    @Autowired
    private EbookSnapshotService ebookSnapshotService;

    @Autowired
    private EbookSnapshotMessageProducer ebookSnapshotMessageProducer;

    @Autowired
    private EbookSnapshotEs ebookSnapshotEs;

    @GetMapping("/es")
    public CommonResp delEs() {
        ebookSnapshotService.delEbookSnapshotES();
        return new CommonResp(true, "");
    }

    @GetMapping("/putEs")
    public CommonResp putEs() {
        ebookSnapshotService.putEbookSnapshotES();
        return new CommonResp(true, "");
    }

    @GetMapping("/get-statistic")
    public CommonResp getStatistic() {
        return new CommonResp(true, "", ebookSnapshotEs.getStatisticES());
    }

    @GetMapping("/get-30-statistic")
    public CommonResp get30Statistic() {
        return new CommonResp(true, "",ebookSnapshotEs.get30StatisticES());
    }

    @GetMapping("/insertEbookSnapshot")
    public CommonResp insertEbookSnapshot() {
        int rows = ebookSnapshotService.insertEbookSnapshot();
        return new CommonResp(true,"",rows);
    }

    @GetMapping("/updateEbookSnapshot")
    public CommonResp updateEbookSnapshot() {
        int rows = ebookSnapshotService.updateEbookSnapshot();
        return new CommonResp(true,"",rows);
    }

    @GetMapping("/updateEbookSnapshotIncrease")
    public CommonResp updateEbookSnapshotIncrease() {
        int rows = ebookSnapshotService.updateEbookSnapshotIncrease();
        return new CommonResp(true,"",rows);
    }

    @PostMapping("/insertProducer")
    public CommonResp insertProducer(String ebookName, String logId) {
        ebookSnapshotMessageProducer.insertEbookSnapshotMessageSend(ebookName,logId);
        return new CommonResp(true, "");
    }

    @PostMapping("/deleteProducer")
    public CommonResp deleteProducer(Long ebookId,String ebookName, String logId) {
        ebookSnapshotMessageProducer.deleteEbookSnapshotMessageSend(ebookId,ebookName,logId);
        return new CommonResp(true, "");
    }


}
