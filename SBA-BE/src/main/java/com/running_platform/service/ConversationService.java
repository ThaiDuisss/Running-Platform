package com.running_platform.service;



import com.running_platform.dto.request.ConversationRequest;
import com.running_platform.dto.response.ConversationResponse;
import com.running_platform.entity.FriendShipAndChat.Conversations;

import java.util.List;

public interface ConversationService {
    ConversationResponse create(ConversationRequest request, Long userId);
    List<ConversationResponse> myConversation(Long userId);
    String generateParticipantHash(List<Long> participantIds);
    ConversationResponse toConversationResponse(Conversations conversation, Long userId);
}
