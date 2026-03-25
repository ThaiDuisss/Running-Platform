package com.running_platform.controller;

import com.running_platform.dto.request.CreateDirectChatRequest;
import com.running_platform.dto.request.CreateGroupRequest;
import com.running_platform.dto.response.ChatMessageResponse;
import com.running_platform.dto.response.ConversationResponse;
import com.running_platform.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getMyConversations(Authentication auth) {
        Long userId = chatService.getCurrentUserId(auth);
        return ResponseEntity.ok(chatService.getUserConversations(userId));
    }

    @PostMapping("/direct")
    public ResponseEntity<ConversationResponse> createOrGetDirectChat(
            Authentication auth,
            @Valid @RequestBody CreateDirectChatRequest request) {
        Long userId = chatService.getCurrentUserId(auth);
        return ResponseEntity.ok(chatService.createOrGetDirectChat(userId, request.getTargetUserId()));
    }

    @PostMapping("/group")
    public ResponseEntity<ConversationResponse> createGroup(
            Authentication auth,
            @Valid @RequestBody CreateGroupRequest request) {
        Long userId = chatService.getCurrentUserId(auth);
        return ResponseEntity.ok(chatService.createGroup(userId, request));
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<Page<ChatMessageResponse>> getMessages(
            Authentication auth,
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        Long userId = chatService.getCurrentUserId(auth);
        return ResponseEntity.ok(chatService.getMessages(userId, id, page, size));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Map<String, String>> addMember(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody Map<String, Long> body) {
        Long userId = chatService.getCurrentUserId(auth);
        chatService.addMemberToGroup(userId, id, body.get("userId"));
        return ResponseEntity.ok(Map.of("message", "Member added successfully"));
    }

    @DeleteMapping("/{id}/leave")
    public ResponseEntity<Map<String, String>> leaveGroup(
            Authentication auth,
            @PathVariable Long id) {
        Long userId = chatService.getCurrentUserId(auth);
        chatService.leaveGroup(userId, id);
        return ResponseEntity.ok(Map.of("message", "Left group successfully"));
    }
}
