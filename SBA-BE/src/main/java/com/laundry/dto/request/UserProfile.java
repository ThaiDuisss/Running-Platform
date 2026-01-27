package com.laundry.dto.request;

import com.laundry.constant.ErrorEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfile {
    Long userId;

    @NotBlank
    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    String fullName;

    @NotBlank
    @Pattern(regexp = "^0[0-9]{9,10}$", message = ErrorEnum.INVALID_PHONE)
    String phoneNumber;

    String avatar;
}
