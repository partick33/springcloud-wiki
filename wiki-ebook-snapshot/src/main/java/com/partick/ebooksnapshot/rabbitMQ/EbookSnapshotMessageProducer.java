package com.partick.ebooksnapshot.rabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EbookSnapshotMessageProducer {
    @Autowired
    RabbitTemplate rabbitTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotMessageProducer.class);

    private final static String EXCHANGE = "wiki.ebookSnapshot";

    public void insertEbookSnapshotMessageSend(String ebookName, String logId) {
        String routeKey = "insert.ebookSnapshot";
        Map map = new HashMap<>(2);
        map.put("ebookName", ebookName);
        map.put("LOG_ID", logId);
        MDC.put("LOG_ID", logId);
        LOG.info("RabbitMQ发送消息：{}", map);
        rabbitTemplate.convertAndSend(EXCHANGE,routeKey,map);
    }

    public void deleteEbookSnapshotMessageSend(Long ebookId,String ebookName, String logId) {
        String routeKey = "delete.ebookSnapshot";
        Map map = new HashMap<>(3);
        map.put("ebookId", ebookId);
        map.put("ebookName", ebookName);
        map.put("LOG_ID", logId);
        MDC.put("LOG_ID", logId);
        LOG.info("RabbitMQ发送消息：{}", map);
        rabbitTemplate.convertAndSend(EXCHANGE,routeKey,map);
    }
}
