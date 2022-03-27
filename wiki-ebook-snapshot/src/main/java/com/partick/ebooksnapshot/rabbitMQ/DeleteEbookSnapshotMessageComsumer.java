package com.partick.ebooksnapshot.rabbitMQ;


import com.partick.ebooksnapshot.service.EbookSnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DeleteEbookSnapshotMessageComsumer {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    EbookSnapshotService ebookSnapshotService;

    private static final Logger LOG = LoggerFactory.getLogger(DeleteEbookSnapshotMessageComsumer.class);

    private final static String EXCHANGE = "wiki.ebookSnapshot";

    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = "wiki.deleteEbook", autoDelete = "true"),
                        exchange = @Exchange(value = EXCHANGE,type = ExchangeTypes.TOPIC),
                        key = "delete.ebookSnapshot"
            )
    )
    @RabbitHandler
    public void deleteEbookMessageReceived(HashMap map) {
        String logId = (String) map.get("LOG_ID");
        MDC.put("LOG_ID", logId);
        LOG.info("RabbitMQ收到消息：{}", map);
        Long ebookId = (Long) map.get("ebookId");
        int rows = ebookSnapshotService.deleteEbookSnapshot(ebookId);
        LOG.info("删除旧的电子书快照：{}本，书名为：{}", rows, map.get("ebookName"));
    }
}
