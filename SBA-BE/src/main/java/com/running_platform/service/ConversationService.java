package com.laundry.service;

import com.laundry.dto.request.ConversationRequest;
import com.laundry.dto.response.ConversationResponse;
import com.laundry.entity.Conversation;

import java.util.List;

public interface ConversationService {
    ConversationResponse create(ConversationRequest request, Long userId);
    List<ConversationResponse> myConversation(Long userId);
    String generateParticipantHash(List<Long> participantIds);
    ConversationResponse toConversationResponse(Conversation conversation, Long userId);
}
