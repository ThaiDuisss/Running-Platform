package com.laundry.mapper;

import com.laundry.dto.response.RoleResponse;
import com.laundry.entity.Roles;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Set<RoleResponse> toResponse (Set<Roles> roles);
}
