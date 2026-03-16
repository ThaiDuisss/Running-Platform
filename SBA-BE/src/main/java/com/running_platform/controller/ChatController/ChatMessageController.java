package com.running_platform.controller.ChatController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.running_platform.dto.request.ChatMessageRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.ChatMessageResponse;
import com.running_platform.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(
                ApiResponse.<ChatMessageResponse>builder()
                        .data(chatMessageService.create(request))
                        .message("Message created successfully")
                        .build()
        );
    }
    @GetMapping("/getMessage/{conversationId}")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getAllMessageByCId(@PathVariable Long conversationId) {
        return ResponseEntity.ok(
                ApiResponse.<List<ChatMessageResponse>>builder()
                        .data(chatMessageService.getMessage(conversationId))
                        .message("Message created successfully")
                        .build()
        );
    }
}
