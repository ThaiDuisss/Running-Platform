package com.running_platform.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileRequest {
    String fullName;
    String phoneNumber;
    String location;
    Double latitude;
    Double longitude;
    String imageUrl;
}
