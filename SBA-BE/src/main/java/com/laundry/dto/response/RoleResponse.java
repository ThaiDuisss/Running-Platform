package com.laundry.dto.response;

import com.laundry.constant.RoleEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    RoleEnum roleName;
    Set<PermissionResponse> permissions;
}
