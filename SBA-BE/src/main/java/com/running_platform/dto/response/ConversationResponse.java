package com.running_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ConversationResponse {
    private Long id;
    private String title;
    @JsonProperty("isGroup")
    private boolean isGroup;
    private String conversationHash;
    private List<ParticipantResponse> participants;
    private ChatMessageResponse lastMessage;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")

    private LocalDateTime updatedAt;
    private int unreadCount;
}