package com.partick.doc.service.impl;


import com.partick.doc.service.WebsocketService;
import com.partick.doc.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WebsocketServiceImpl implements WebsocketService {
    @Autowired
    WebSocketServer webSocketServer;

    @Override
    @Async
    public void sendInfo(String name, String logId) {
        MDC.put("LOG_ID", logId);
        webSocketServer.sendInfo(name);
    }
}
