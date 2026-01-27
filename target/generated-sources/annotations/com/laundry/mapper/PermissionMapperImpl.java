package com.laundry.mapper;

import com.laundry.dto.response.PermissionResponse;
import com.laundry.entity.Permissions;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-03T21:25:08+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class PermissionMapperImpl implements PermissionMapper {

    @Override
    public PermissionResponse toResponse(Permissions permissions) {
        if ( permissions == null ) {
            return null;
        }

        PermissionResponse.PermissionResponseBuilder permissionResponse = PermissionResponse.builder();

        permissionResponse.name( permissions.getName() );
        permissionResponse.description( permissions.getDescription() );

        return permissionResponse.build();
    }
}
