package com.laundry.mapper;

import com.laundry.dto.response.PermissionResponse;
import com.laundry.entity.Permissions;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponse toResponse (Permissions permissions);
}
