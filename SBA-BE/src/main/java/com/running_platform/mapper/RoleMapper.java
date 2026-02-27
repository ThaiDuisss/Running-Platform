package com.running_platform.mapper;

import com.running_platform.dto.response.RoleResponse;
import com.running_platform.entity.UserAuth.Roles;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Set<RoleResponse> toResponse (Set<Roles> roles);
}
