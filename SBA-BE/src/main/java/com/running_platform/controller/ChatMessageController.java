package com.running_platform.controller;

import com.running_platform.dto.request.ChatMessageRequest;
import com.running_platform.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageRequest request, Principal principal) {
        Authentication auth = (Authentication) principal;
        Long senderId = chatService.getCurrentUserId(auth);
        log.info("Received message from userId={}: {}", senderId, request.getContent());
        chatService.sendMessage(senderId, request);
    }

    @MessageMapping("/chat.typing")
    public void typingIndicator(@Payload Map<String, Object> payload, Principal principal) {
        Authentication auth = (Authentication) principal;
        Long senderId = chatService.getCurrentUserId(auth);
        Long conversationId = Long.parseLong(payload.get("conversationId").toString());

        // Broadcast typing indicator đến tất cả người trong conversation
        messagingTemplate.convertAndSend(
                "/topic/conversation." + conversationId + ".typing",
                Map.of("userId", senderId, "typing", true)
        );
    }
}