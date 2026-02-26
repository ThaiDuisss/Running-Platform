package com.laundry.dto.request;

import com.laundry.constant.RoleEnum;
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
    Set<RoleEnum> roles;
    Boolean emailVerified;
}