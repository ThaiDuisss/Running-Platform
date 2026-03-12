package com.laundry.service.impl;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laundry.dto.request.ChatMessageRequest;
import com.laundry.dto.response.ChatMessageResponse;
import com.laundry.entity.ChatMessage;
import com.laundry.entity.ParticipantInfo;
import com.laundry.entity.WebsocketSession;
import com.laundry.enums.ErrorEnum;
import com.laundry.exception.AppException;
import com.laundry.httpClient.ProfileClient;
import com.laundry.mapper.ChatMessageMapper;
import com.laundry.repository.ChatMessageRepository;
import com.laundry.repository.ConversationRepository;
import com.laundry.repository.WebSocketSessionRepository;
import com.laundry.service.ChatMessageService;
import com.laundry.service.DateTimeFormatter;
import com.laundry.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
@Slf4j(topic = "CHAT_MESSAGE_SERVICE")
public class ChatMessageServiceImp implements ChatMessageService {
    ConversationRepository conversationRepository;
    ProfileClient profileClient;
    ChatMessageMapper chatMessageMapper;
    ChatMessageRepository chatMessageRepository;
    SocketIOServer server;
    WebSocketSessionRepository webSocketSessionRepository;
    ObjectMapper objectMapper;

    @Override
    public ChatMessageResponse create(ChatMessageRequest request, Long userID) throws JsonProcessingException {
        //Valid conversationId
        var conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new AppException(ErrorEnum.CONVERSATION_NOT_FOUND));
        conversation.getParticipants()
                .stream()
                .filter(t -> userID.equals(t.getUserId()))
                .findAny().orElseThrow(() -> new AppException(ErrorEnum.CONVERSATION_NOT_FOUND));
        //Get userInfo from ProfileService
        var userInfoResponse = profileClient.getById(userID).getBody().getData();
        //Build ChatMessage Info
        ChatMessage chatMessage = chatMessageMapper.toChatMessage(request);
        chatMessage.setSender(ParticipantInfo.builder()
                .userId(userID)
                .avatar(userInfoResponse.getAvatar())
                .fullName(userInfoResponse.getFullName())
                .build());
        chatMessage.setCreateDate(Instant.now());
        chatMessage = chatMessageRepository.save(chatMessage);
        ChatMessageResponse chatMessageResponse = toChatMessageResponse(chatMessage);
        String message = objectMapper.writeValueAsString(chatMessage.getMessage());

        List<Long> userId = conversation.getParticipants().stream().map(ParticipantInfo::getUserId).toList();
        Map<String, WebsocketSession> listSession = webSocketSessionRepository.findAllByUserIdIn(userId)
                .stream().collect(Collectors.toMap(WebsocketSession::getSocketSessionId, Function.identity()));
        server.getAllClients().forEach(client -> {
            String sessionId = client.getSessionId().toString();
            String mess = "";
            var variable = listSession.get(sessionId);
            if(!Objects.isNull(variable)) {
                try {
                    chatMessageResponse.setMe(variable.getUserId().equals(userID));
                    mess = objectMapper.writeValueAsString(chatMessageResponse);
                    client.sendEvent("message", mess);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        //Create message to mongoDb

        //Convert to Response
        return toChatMessageResponse(chatMessage);
    }

    @Override
    public List<ChatMessageResponse> getMessage(String conversationId, Long UserId) {
        conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorEnum.CONVERSATION_NOT_FOUND))
                .getParticipants().stream()
                .filter(t -> userId.equals(t.getUserId()))
                .findAny().orElseThrow(() -> new AppException(ErrorEnum.CONVERSATION_NOT_FOUND));
        List<ChatMessage> chatMessages = chatMessageRepository.findByConversationId(conversationId);
        return chatMessages.stream().map(this::toChatMessageResponse).toList();
    }

    private ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
        //kiểm tra xem người gửi có phải là người dùng hiện tại không
        Long userID = SecurityUtils.getUserId();
        var chatMessageResponse = chatMessageMapper.toChatMessageResponse(chatMessage);
        chatMessageResponse.setMe(userID.equals(chatMessage.getSender().getUserId()));
        chatMessageResponse.setCreateDate(chatMessage.getCreateDate());
        chatMessageResponse.setCreated(DateTimeFormatter.formatDateTime(chatMessage.getCreateDate()));
        return chatMessageResponse;
    }
}
