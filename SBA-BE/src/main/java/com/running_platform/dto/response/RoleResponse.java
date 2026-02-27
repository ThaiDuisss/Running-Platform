package com.running_platform.dto.response;

import com.running_platform.constant.RoleEnum;
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
