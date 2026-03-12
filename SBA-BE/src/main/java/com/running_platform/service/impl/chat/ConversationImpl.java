package com.laundry.service.impl;

import com.laundry.dto.request.ConversationRequest;
import com.laundry.dto.response.ConversationResponse;
import com.laundry.entity.Conversation;
import com.laundry.entity.ParticipantInfo;
import com.laundry.httpClient.ProfileClient;
import com.laundry.mapper.ConversationMapper;
import com.laundry.repository.ConversationRepository;
import com.laundry.service.ConversationService;
import com.laundry.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.StringJoiner;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ConversationImpl implements ConversationService {
    ProfileClient profileClient;
    ConversationRepository conversationRepository;
    ConversationMapper conversationMapper;
    @Override
    public ConversationResponse create(ConversationRequest request, Long userID) {
        //Fetch user info
        var userInfoResponse = profileClient.getById(userID).getBody().getData();
        var participant = profileClient.getById(request.getParticipantIds().get(0)).getBody().getData();
        List<Long> userIds = List.of(userID, participant.getUserId());
        var sortedIds = userIds.stream().sorted().toList();
        String participantHash = generateParticipantHash(sortedIds);
        var  conversation = conversationRepository.findByParticipantHash(participantHash).orElseGet(
                () -> {List<ParticipantInfo> participantInfos = List.of(
                        ParticipantInfo.builder()
                                .userId(userID)
                                .fullName(userInfoResponse.getFullName())
                                .avatar(userInfoResponse.getAvatar())
                                .build(),
                        ParticipantInfo.builder()
                                .userId(participant.getUserId())
                                .fullName(participant.getFullName())
                                .avatar(participant.getAvatar())
                                .build()
                );

                    //Build conversation info
                    Conversation newConversation = Conversation.builder()
                            .type(request.getType())
                            .participantHash(participantHash)
                            .createDate(Instant.now())
                            .modifiedDate(Instant.now())
                            .participants(participantInfos)
                            .build();
                    return conversationRepository.save(newConversation);
                });

        return toConversationResponse(conversation);
    }

    @Override
    public List<ConversationResponse> myConversation(Long userID) {
        List<Conversation> conversations = conversationRepository.findAllByParticipantsUserId(userID);

        return conversations.stream().map(this::toConversationResponse).toList();
    }

    @Override
    public String generateParticipantHash(List<Long> participantIds) {
        StringJoiner joiner = new StringJoiner("_");

        //map sang String để add vô joiner
        participantIds.stream().map(String::valueOf).forEach(joiner :: add);
        return joiner.toString()    ;
    }

    @Override
    public ConversationResponse toConversationResponse(Conversation conversation, Long userID) {
        ConversationResponse conversationResponse = conversationMapper.toResponse(conversation);
        //đổi avatar và name của người tham gia hội thoại
        conversation.getParticipants().stream().filter(
                t -> !t.getUserId().equals(userID)).findFirst().ifPresent(
                t -> {
                    conversationResponse.setConversationAvatar(t.getAvatar());
                    conversationResponse.setConversationName(t.getFullName());
                }
        );
        return conversationResponse;
    }
}
