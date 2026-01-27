package com.laundry.dto.request;

import com.laundry.enums.ErrorEnum;
import com.laundry.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @NotBlank
    @Size(min = 6, max = 30)
    @Email
    String username;

    @NotBlank
    @Size(min = 6)
    String password;

    @NotBlank
    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    String fullName;

    @NotBlank
    Set<RoleEnum> roles;
}
