package com.running_platform.controller.ChatController;


import com.running_platform.dto.request.ConversationRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.ConversationResponse;
import com.running_platform.service.ConversationService;
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
@RequestMapping
public class ConversationController {
    ConversationService conversationService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ConversationResponse>> createConversation(@RequestBody @Valid ConversationRequest request) {
//       Long userId = SecurityUtils.getIdFromHeader();
//        return ResponseEntity.ok(
//                ApiResponse.<ConversationResponse>builder()
//                        .data(conversationService.create(request, userId))
//                        .message("Conversation created successfully")
//                        .build()
//        );
        return null;
    }

    @GetMapping("/my-conversations")
    public ResponseEntity<ApiResponse<List<ConversationResponse>>> createConversation() {
//        Long userId = SecurityUtils.getIdFromHeader();
//        return ResponseEntity.ok(
//                ApiResponse.<List<ConversationResponse>>builder()
//                        .data(conversationService.myConversation(userId))
//                        .message("Conversation created successfully")
//                        .build()
//        );
        return null;
    }
}
