package com.laundry.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    @NotBlank
    @Size(min = 6, max = 30)
    @Email
    String username;
    @NotBlank
    @Size(min = 6, max = 30)
    String password;
}
