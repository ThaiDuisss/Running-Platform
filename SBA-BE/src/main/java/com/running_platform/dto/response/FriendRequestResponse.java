package com.running_platform.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendRequestResponse {
    Long id; 
    Long userId;
    String username;
    String fullName;
    String imageUrl;
}