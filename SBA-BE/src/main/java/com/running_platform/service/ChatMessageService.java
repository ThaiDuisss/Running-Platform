package com.laundry.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.laundry.dto.request.ChatMessageRequest;
import com.laundry.dto.response.ChatMessageResponse;

import java.util.List;

public interface ChatMessageService {
    ChatMessageResponse create (ChatMessageRequest request, Long userId) throws JsonProcessingException;
    List<ChatMessageResponse> getMessage(String conversationId, Long userId);
}
