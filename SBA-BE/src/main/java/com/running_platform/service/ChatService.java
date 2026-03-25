package com.running_platform.service;

import com.running_platform.dto.request.*;
import com.running_platform.dto.response.ChatMessageResponse;
import com.running_platform.dto.response.ConversationResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ChatService {

    List<ConversationResponse> getUserConversations(Long userId);

    ConversationResponse createOrGetDirectChat(Long currentUserId, Long targetUserId);

    ConversationResponse createGroup(Long currentUserId, CreateGroupRequest request);

    ChatMessageResponse sendMessage(Long senderId, ChatMessageRequest request);

    Page<ChatMessageResponse> getMessages(Long userId, Long conversationId, int page, int size);

    void addMemberToGroup(Long adminId, Long conversationId, Long newMemberId);

    void leaveGroup(Long userId, Long conversationId);

    Long getCurrentUserId(Authentication authentication);
}