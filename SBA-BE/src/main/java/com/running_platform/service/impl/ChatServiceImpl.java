package com.running_platform.service.impl;

import com.running_platform.dto.request.*;
import com.running_platform.dto.response.ChatMessageResponse;
import com.running_platform.dto.response.ConversationResponse;
import com.running_platform.dto.response.ParticipantResponse;
import com.running_platform.entity.FriendShipAndChat.ChatMessage;
import com.running_platform.entity.FriendShipAndChat.ConversationParticipants;
import com.running_platform.entity.FriendShipAndChat.Conversations;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.MessageType;
import com.running_platform.repository.ChatMessageRepository;
import com.running_platform.repository.ConversationParticipantsRepository;
import com.running_platform.repository.ConversationRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.security.CustomUserDetails;
import com.running_platform.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ConversationParticipantsRepository participantsRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional(readOnly = true)
    public List<ConversationResponse> getUserConversations(Long userId) {
        log.info("Get conversations for userId={}", userId);
        List<Conversations> conversations = conversationRepository.findAllByParticipantUserId(userId);
        log.info("Conversations = {}", conversations.size());
        return conversations.stream()
                .map(c -> buildConversationResponse(c, userId))
                .collect(Collectors.toList());
    }

    @Override
    public ConversationResponse createOrGetDirectChat(Long currentUserId, Long targetUserId) {
        String hash = generateDirectChatHash(currentUserId, targetUserId);

        return conversationRepository.findByConversationHash(hash)
                .map(c -> buildConversationResponse(c, currentUserId))
                .orElseGet(() -> {
                    Users currentUser = userRepository.findById(currentUserId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Users targetUser = userRepository.findById(targetUserId)
                            .orElseThrow(() -> new RuntimeException("Target user not found"));

                    Conversations conversation = Conversations.builder()
                            .title(null) // null = direct chat
                            .isGroup(false)
                            .conversationHash(hash)
                            .build();
                    conversation = conversationRepository.save(conversation);

                    addParticipant(conversation, currentUser, true);
                    addParticipant(conversation, targetUser, false);

                    return buildConversationResponse(conversation, currentUserId);
                });
    }

    @Override
    public ConversationResponse createGroup(Long currentUserId, CreateGroupRequest request) {
        Users creator = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> allMemberIds = new ArrayList<>(request.getMemberIds());
        if (!allMemberIds.contains(currentUserId)) {
            allMemberIds.add(0, currentUserId);
        }

        String hash = UUID.randomUUID().toString();

        Conversations group = Conversations.builder()
                .title(request.getTitle())
                .isGroup(true)
                .conversationHash(hash)
                .build();
        group = conversationRepository.save(group);

        final Conversations savedGroup = group;
        addParticipant(savedGroup, creator, true);

        for (Long memberId : request.getMemberIds()) {
            if (!memberId.equals(currentUserId)) {
                Users member = userRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException("Member not found: " + memberId));
                addParticipant(savedGroup, member, false);
            }
        }

        ConversationResponse response = buildConversationResponse(savedGroup, currentUserId);
        notifyNewConversation(allMemberIds, response);

        return response;
    }

    @Override
    public ChatMessageResponse sendMessage(Long senderId, ChatMessageRequest request) {
        if (!conversationRepository.isUserInConversation(request.getConversationId(), senderId)) {
            throw new RuntimeException("User is not a member of this conversation");
        }

        Users sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Conversations conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .conversation(conversation)
                .content(request.getContent())
                .type(request.getType() != null ? request.getType() : MessageType.TEXT)
                .build();

        message = chatMessageRepository.save(message);
        ChatMessageResponse response = buildMessageResponse(message);

        messagingTemplate.convertAndSend(
                "/topic/conversation." + request.getConversationId(),
                response
        );

        conversation.setUpdateDate(Instant.now());
        conversationRepository.save(conversation);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageResponse> getMessages(Long userId, Long conversationId, int page, int size) {
        if (!conversationRepository.isUserInConversation(conversationId, userId)) {
            throw new RuntimeException("Access denied");
        }

        return chatMessageRepository
                .findByConversationIdOrderByCreatDateDesc(conversationId, PageRequest.of(page, size))
                .map(this::buildMessageResponse);
    }

    @Override
    public void addMemberToGroup(Long adminId, Long conversationId, Long newMemberId) {
        ConversationParticipants adminParticipant = participantsRepository
                .findByUserIdAndConversationId(adminId, conversationId)
                .orElseThrow(() -> new RuntimeException("Admin not found in conversation"));

        if (!adminParticipant.isAdmin()) {
            throw new RuntimeException("Only admins can add members");
        }

        Users newMember = userRepository.findById(newMemberId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Conversations conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        if (!conversation.isGroup()) {
            throw new RuntimeException("Cannot add member to direct chat");
        }

        if (participantsRepository.existsByUserIdAndConversationId(newMemberId, conversationId)) {
            throw new RuntimeException("User already in conversation");
        }

        addParticipant(conversation, newMember, false);

        messagingTemplate.convertAndSend(
                "/topic/conversation." + conversationId + ".members",
                Map.of("event", "MEMBER_ADDED", "userId", newMemberId, "username", newMember.getUsername())
        );
    }

    @Override
    public void leaveGroup(Long userId, Long conversationId) {
        ConversationParticipants participant = participantsRepository
                .findByUserIdAndConversationId(userId, conversationId)
                .orElseThrow(() -> new RuntimeException("User not in conversation"));

        participantsRepository.delete(participant);

        messagingTemplate.convertAndSend(
                "/topic/conversation." + conversationId + ".members",
                Map.of("event", "MEMBER_LEFT", "userId", userId)
        );
    }

    @Override
    public Long getCurrentUserId(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUserEntity().getId();
    }

    // ================= Helper Methods =================

    private void addParticipant(Conversations conversation, Users user, boolean isAdmin) {
        ConversationParticipants participant = ConversationParticipants.builder()
                .conversation(conversation)
                .user(user)
                .isAdmin(isAdmin)
                .build();
        participantsRepository.save(participant);
    }

    private ConversationResponse buildConversationResponse(Conversations c, Long currentUserId) {
        List<ConversationParticipants> participants = participantsRepository.findByConversationId(c.getId());

        List<ParticipantResponse> participantResponses = participants.stream()
                .map(p -> ParticipantResponse.builder()
                        .userId(p.getUser().getId())
                        .username(p.getUser().getUsername())
                        .fullName(p.getUser().getFullName())
                        .imageUrl(p.getUser().getImageUrl())
                        .isAdmin(p.isAdmin())
                        .build())
                .collect(Collectors.toList());

        String title = c.getTitle();
        if (!c.isGroup() && title == null) {
            title = participants.stream()
                    .filter(p -> !p.getUser().getId().equals(currentUserId))
                    .map(p -> p.getUser().getFullName() != null ? p.getUser().getFullName() : p.getUser().getUsername())
                    .findFirst()
                    .orElse("Unknown");
        }

        ChatMessageResponse lastMsg = chatMessageRepository
                .findLastMessageByConversationId(c.getId())
                .map(this::buildMessageResponse)
                .orElse(null);

        return ConversationResponse.builder()
                .id(c.getId())
                .title(title)
                .isGroup(c.isGroup())
                .conversationHash(c.getConversationHash())
                .participants(participantResponses)
                .lastMessage(lastMsg)
                .updatedAt(
                        c.getUpdateDate() != null
                                ? c.getUpdateDate().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime()
                                : null
                )
                .build();
    }

    private ChatMessageResponse buildMessageResponse(ChatMessage m) {
        return ChatMessageResponse.builder()
                .id(m.getId())
                .conversationId(m.getConversation().getId())
                .senderId(m.getSender().getId())
                .senderName(m.getSender().getFullName() != null ? m.getSender().getFullName() : m.getSender().getUsername())
                .senderAvatar(m.getSender().getImageUrl())
                .content(m.getContent())
                .type(m.getType())
                .createdAt(
                        m.getCreatDate() != null
                                ? m.getCreatDate().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime()
                                : null
                )
                .build();
    }

    private String generateDirectChatHash(Long userId1, Long userId2) {
        long min = Math.min(userId1, userId2);
        long max = Math.max(userId1, userId2);
        return "direct_" + min + "_" + max;
    }

    private void notifyNewConversation(List<Long> memberIds, ConversationResponse response) {
        for (Long memberId : memberIds) {
            messagingTemplate.convertAndSendToUser(
                    memberId.toString(),
                    "/queue/conversations",
                    response
            );
        }
    }
}