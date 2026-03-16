package com.running_platform.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
@Getter
@Setter
@FieldDefaults(level =  AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    Long id;
    String conversationId;
//    ParticipantInfo sender;
    boolean me;
    String message;
    String created;
    Instant createDate;

}
