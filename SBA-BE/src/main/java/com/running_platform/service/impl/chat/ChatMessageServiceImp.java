package com.running_platform.service.impl.chat;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.ChatMessageRequest;
import com.running_platform.dto.response.ChatMessageResponse;
import com.running_platform.entity.FriendShipAndChat.ChatMessage;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.ChatMessageMapper;
import com.running_platform.repository.ChatMessageRepository;
import com.running_platform.repository.ConversationRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.security.AppSecurityUtils;
import com.running_platform.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
@Slf4j(topic = "CHAT_MESSAGE_SERVICE")
public class ChatMessageServiceImp implements ChatMessageService {
    ConversationRepository conversationRepository;
    ChatMessageMapper  chatMessageMapper;
    ChatMessageRepository chatMessageRepository;
//    SocketIOServer server;
    ObjectMapper objectMapper;
    UserRepository userRepository;
    @Override
    public ChatMessageResponse create(ChatMessageRequest request) throws JsonProcessingException {
//        //Valid conversationId
//        Optional<Long> id = AppSecurityUtils.getCurrentUserId();
//        if(id.isPresent()) {
//            Users u = userRepository.findById(id.get()).orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
//
//            var conversation = conversationRepository.findById(request.getConversationId())
//                    .orElseThrow(() -> new AppException(ErrorEnum.CONVERSATION_NOT_FOUND));
//            conversation.getConversationParticipants()
//                    .stream()
//                    .filter(t -> u.getId().toString().equals(t.getUser().getId().toString()))
//                    .findAny().orElseThrow(() -> new AppException(ErrorEnum.CONVERSATION_NOT_FOUND));
//
//            //Get userInfo from ProfileService
//            //Build ChatMessage Info
//            ChatMessage chatMessage = chatMessageMapper.toEntity(request);
//            chatMessage.setSender(u);
//            chatMessage = chatMessageRepository.save(chatMessage);
//            ChatMessageResponse chatMessageResponse = toChatMessageResponse(chatMessage);
//
//        String message = objectMapper.writeValueAsString(chatMessage.getMessage());
//
//        List<Long> userId = conversation.getParticipants().stream().map(ParticipantInfo::getUserId).toList();
//        Map<String, WebsocketSession> listSession = webSocketSessionRepository.findAllByUserIdIn(userId)
//                .stream().collect(Collectors.toMap(WebsocketSession::getSocketSessionId, Function.identity()));
//        server.getAllClients().forEach(client -> {
//            String sessionId = client.getSessionId().toString();
//            String mess = "";
//            var variable = listSession.get(sessionId);
//            if(!Objects.isNull(variable)) {
//                try {
//                    chatMessageResponse.setMe(variable.getUserId().equals(userID));
//                    mess = objectMapper.writeValueAsString(chatMessageResponse);
//                    client.sendEvent("message", mess);
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//
//        });
//        //Create message to mongoDb
//
//        //Convert to Response
//        return toChatMessageResponse(chatMessage);}
//        throw new AppException(ErrorEnum.UNKNOWN_ERROR);
        return null;
    }

    @Override
    public List<ChatMessageResponse> getMessage(Long conversationId) {
        Optional<Long> id = AppSecurityUtils.getCurrentUserId();
        if(id.isPresent()) {
            conversationRepository.findById(conversationId)
                    .orElseThrow(() -> new AppException(ErrorEnum.CONVERSATION_NOT_FOUND))
                    .getConversationParticipants().stream()
                    .filter(t -> id.get().toString().equals(t.getId().toString()))
                    .findAny().orElseThrow(() -> new AppException(ErrorEnum.CONVERSATION_NOT_FOUND));
            List<ChatMessage> chatMessages = chatMessageRepository.findByConversationIdOrderByCreatDateDesc(conversationId);
            return chatMessages.stream().map(this::toChatMessageResponse).toList();
        }
        throw new AppException(ErrorEnum.UNKNOWN_ERROR);
    }

    private ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
        //kiểm tra xem người gửi có phải là người dùng hiện tại không
        Optional<Long> id = AppSecurityUtils.getCurrentUserId();
        if(id.isPresent()) {
            var chatMessageResponse = chatMessageMapper.EntityToRespond(chatMessage);
            chatMessageResponse.setMe(id.toString().equals(chatMessage.getSender().getId().toString()));
            chatMessageResponse.setCreateDate(chatMessage.getCreatDate());
//            chatMessageResponse.setCreated(DateTimeFormatter.formatDateTime(chatMessage.getCreateDate()));
            return chatMessageResponse;
        }
            throw new AppException(ErrorEnum.UNKNOWN_ERROR);
    }
}
