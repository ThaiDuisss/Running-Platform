package com.running_platform.dto.request;

import com.running_platform.constant.RoleEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AdminUpdateUserRequest {

    String username;
    String phoneNumber;
    RoleEnum role;
}