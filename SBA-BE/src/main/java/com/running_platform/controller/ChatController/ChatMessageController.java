package com.laundry.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.laundry.dto.request.ChatMessageRequest;
import com.laundry.dto.response.ApiResponse;
import com.laundry.dto.response.ChatMessageResponse;
import com.laundry.entity.ChatMessage;
import com.laundry.service.ChatMessageService;
import com.laundry.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping("/messages")
public class ChatMessageController {
    ChatMessageService chatMessageService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> createMessage(@RequestBody @Valid ChatMessageRequest request) throws JsonProcessingException {
        Long userId = SecurityUtils.getIdFromHeader();
        return ResponseEntity.ok(
                ApiResponse.<ChatMessageResponse>builder()
                        .data(chatMessageService.create(request, userId))
                        .message("Message created successfully")
                        .build()
        );
    }
    @GetMapping("/getMessage/{conversationId}")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getAllMessageByCId(@PathVariable("conversationId") String conversationId) {
        Long userId = SecurityUtils.getIdFromHeader();
        return ResponseEntity.ok(
                ApiResponse.<List<ChatMessageResponse>>builder()
                        .data(chatMessageService.getMessage(conversationId, userId))
                        .message("Message created successfully")
                        .build()
        );
    }
}
