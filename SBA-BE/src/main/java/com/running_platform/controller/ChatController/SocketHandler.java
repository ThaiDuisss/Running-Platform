package com.laundry.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "SocketHandler")
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SocketHandler {

    SocketIOServer server;

    @OnConnect
    public void clientConnect (SocketIOClient client) {

    }

    @OnDisconnect
    public void disconnect(SocketIOClient client) {

    }

    @PostConstruct
    public void startSever(){
        server.addListeners(this);

        server.start();
        log.info("Socket.IO server started");
    }
    @PreDestroy
    public void stopServer() {
        server.stop();
        log.info("Socket.IO server stopped");
    }

 }

