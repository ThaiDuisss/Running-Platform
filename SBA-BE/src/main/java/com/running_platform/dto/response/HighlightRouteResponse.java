package com.running_platform.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HighlightRouteResponse {
    Long id;

    String title;

    String location;

    String distanceLabel;

    Integer priority;

    Boolean isActive;

    String thumbnail;

    String polyline;
}
