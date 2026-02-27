package com.running_platform.mapper;

import com.running_platform.dto.response.PermissionResponse;
import com.running_platform.entity.UserAuth.Permissions;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponse toResponse (Permissions permissions);
}
