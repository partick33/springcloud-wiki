package com.partick.doc.rabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VoteMessageProducer {
    @Autowired
    RabbitTemplate rabbitTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(VoteMessageProducer.class);

    private final static String EXCHANGE = "";
    private final static String QUEUE_NAME = "vote_docName";

    public void voteMessageSend(String message, String logId) {
        Map map = new HashMap<>(2);
        message = "【" + message + "】被点赞！";
        map.put("message", message);
        map.put("LOG_ID", logId);
        MDC.put("LOG_ID", logId);
        LOG.info("RabbitMQ发送消息：{}", map);
        rabbitTemplate.convertAndSend(EXCHANGE,QUEUE_NAME,map);
    }

}
