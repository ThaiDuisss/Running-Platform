package com.laundry.dto.response;

import com.laundry.entity.ParticipantInfo;
import com.laundry.enums.ConversationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
@FieldDefaults(level =  AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {
    String id;
    ConversationType type;
    String participantHash;
    String conversationAvatar;
    String conversationName;
    List<ParticipantInfo> participants;
    Instant createDate;
    Instant modifiedDate;
}
