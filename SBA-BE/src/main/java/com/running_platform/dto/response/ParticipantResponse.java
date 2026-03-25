package com.running_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantResponse {
    private Long userId;
    private String username;
    private String fullName;
    private String imageUrl;
    @JsonProperty("isAdmin")

    private boolean isAdmin;
    private boolean isOnline;
}
