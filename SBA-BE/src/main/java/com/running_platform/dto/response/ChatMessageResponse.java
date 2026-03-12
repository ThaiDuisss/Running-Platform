package com.laundry.dto.response;

import com.laundry.entity.ParticipantInfo;
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
    String id;
    String conversationId;
    ParticipantInfo sender;
    boolean me;
    String message;
    String created;
    Instant createDate;

}
