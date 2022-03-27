package com.partick.doc.rabbitMQ;


import com.partick.doc.service.WebsocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class VoteMessageComsumer {
    @Autowired
    WebsocketService websocketService;

    private static final Logger LOG = LoggerFactory.getLogger(VoteMessageComsumer.class);

    private final static String QUEUE_NAME = "vote_docName";

    @RabbitListener(queuesToDeclare = @Queue(QUEUE_NAME))
    public void voteMessageReceived(HashMap<String,String> map) {
        String logId = (String) map.get("LOG_ID");
        MDC.put("LOG_ID", logId);
        LOG.info("RabbitMQ收到消息：{}", map);
        websocketService.sendInfo((String)map.get("message"), logId);
    }
}
