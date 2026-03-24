package com.running_platform.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowNetworkResponse {
    PageResponse<FriendResponse> page;
    long discoverCount;
    long followingCount;
    long followersCount;
}
