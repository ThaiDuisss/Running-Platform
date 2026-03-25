package com.running_platform.dto.response;

import com.running_platform.enums.TabEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendResponse {
    Long id;
    String username;
    String fullName;
    String phoneNumber;
    String imageUrl;
    String location;
    String headline;
    Double distanceKm;
    TabEnum relation;
}
