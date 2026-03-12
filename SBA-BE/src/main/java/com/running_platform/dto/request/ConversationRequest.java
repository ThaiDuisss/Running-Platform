package com.laundry.dto.request;

import com.laundry.entity.ParticipantInfo;
import com.laundry.enums.ConversationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Getter
@Setter
@FieldDefaults(level =  AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRequest {
    ConversationType type;
    @Size(min = 1)
    @NotNull
    List<Long> participantIds;
}
