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

    String location;

    String fullName;

    String imageUrl;

    String address;

    LocalDateTime createAt;

    private SecurityEnums.AuthProviderId registeredProviderName;

    private String registeredProviderId;

    LocalDateTime vipExpiredAt;

    String phoneNumber;
}