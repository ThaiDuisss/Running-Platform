package com.running_platform.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDirectChatRequest {
    @NotNull
    private Long targetUserId;
}