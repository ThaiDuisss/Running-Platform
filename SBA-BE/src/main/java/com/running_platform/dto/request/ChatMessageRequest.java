package com.running_platform.dto.request;

import com.running_platform.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatMessageRequest {
    @NotNull
    private Long conversationId;
    @NotBlank
    private String content;
    private MessageType type = MessageType.TEXT;
}