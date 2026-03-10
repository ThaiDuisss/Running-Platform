package com.running_platform.dto.request;

import com.running_platform.constant.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCreateUserRequest {

    @NotBlank
    @Email
    String username;

    @NotBlank
    @Size(min = 6)
    String password;

    @NotBlank
    String fullName;

    String phoneNumber;

    @NotEmpty
    Set<RoleEnum> roles;
}