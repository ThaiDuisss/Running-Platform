package com.laundry.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {
    Long userId;
    String username;
    String fullName;
    String phoneNumber;
    Set<RoleResponse> role;
    String avatar;
}
