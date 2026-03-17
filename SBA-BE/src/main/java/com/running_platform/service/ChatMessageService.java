package com.running_platform.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.running_platform.dto.request.ChatMessageRequest;
import com.running_platform.dto.response.ChatMessageResponse;


import java.util.List;

public interface ChatMessageService {
    ChatMessageResponse create (ChatMessageRequest request) throws JsonProcessingException;
    List<ChatMessageResponse> getMessage(Long conversationId);
}
