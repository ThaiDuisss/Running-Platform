package com.running_platform.dto.response;


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
    Long id;
//    ConversationType type;
    String participantHash;
    String conversationAvatar;
    String conversationName;
//    List<ParticipantInfo> participants;
    Instant createDate;
    Instant modifiedDate;
}
