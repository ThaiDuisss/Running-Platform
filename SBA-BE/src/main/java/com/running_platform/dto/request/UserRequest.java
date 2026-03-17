package com.running_platform.dto.request;


import com.running_platform.constant.ErrorEnum;
import com.running_platform.constant.RoleEnum;
import com.running_platform.security.Oauth2.common.SecurityEnums;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @NotBlank
    @Size(min = 6, max = 30)
    @Email(message = "Invalid email format")
    String username;

    @NotBlank
    @Size(min = 6, message= ErrorEnum.INVALID_PASSWORD)
    String password;

    @NotBlank
    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    String fullName;

    private String imageUrl;

     boolean emailVerified = false;

    @Nullable
    private SecurityEnums.AuthProviderId registeredProviderName = SecurityEnums.AuthProviderId.app_custom_authentication;

    @Nullable
    private String registeredProviderId;

    @Pattern(regexp = "^0[0-9]{9,10}$", message = ErrorEnum.INVALID_PHONE)
    String phoneNumber;

    Set<RoleEnum> roles;

}
