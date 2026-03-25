package com.running_platform.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import com.running_platform.security.Oauth2.common.SecurityEnums;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;

    String username;

    boolean emailVerified;

    Set<RoleResponse> roles;

    private Double latitude;
    private Double longitude;

    String fullName;

    String imageUrl;

    String location;

    LocalDateTime createAt;

    private SecurityEnums.AuthProviderId registeredProviderName;

    private String registeredProviderId;

    LocalDateTime vipExpiredAt;

    String phoneNumber;
}