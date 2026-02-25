package com.laundry.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
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

    String latitude;

    String longitude;

    String avatar;

    LocalDateTime createAt;

    LocalDateTime vipExpiredAt;

    String phoneNumber;
}