package com.running_platform.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HighlightRouteRequest {
    String title;

    String location;

    String distanceLabel;

    Integer priority;

    Boolean isActive;
}
