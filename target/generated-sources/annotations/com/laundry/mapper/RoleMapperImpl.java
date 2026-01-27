package com.laundry.mapper;

import com.laundry.dto.response.PermissionResponse;
import com.laundry.dto.response.RoleResponse;
import com.laundry.entity.Permissions;
import com.laundry.entity.Roles;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-03T21:25:08+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Set<RoleResponse> toResponse(Set<Roles> roles) {
        if ( roles == null ) {
            return null;
        }

        Set<RoleResponse> set = new LinkedHashSet<RoleResponse>( Math.max( (int) ( roles.size() / .75f ) + 1, 16 ) );
        for ( Roles roles1 : roles ) {
            set.add( rolesToRoleResponse( roles1 ) );
        }

        return set;
    }

    protected PermissionResponse permissionsToPermissionResponse(Permissions permissions) {
        if ( permissions == null ) {
            return null;
        }

        PermissionResponse.PermissionResponseBuilder permissionResponse = PermissionResponse.builder();

        permissionResponse.name( permissions.getName() );
        permissionResponse.description( permissions.getDescription() );

        return permissionResponse.build();
    }

    protected Set<PermissionResponse> permissionsSetToPermissionResponseSet(Set<Permissions> set) {
        if ( set == null ) {
            return null;
        }

        Set<PermissionResponse> set1 = new LinkedHashSet<PermissionResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Permissions permissions : set ) {
            set1.add( permissionsToPermissionResponse( permissions ) );
        }

        return set1;
    }

    protected RoleResponse rolesToRoleResponse(Roles roles) {
        if ( roles == null ) {
            return null;
        }

        RoleResponse.RoleResponseBuilder roleResponse = RoleResponse.builder();

        roleResponse.roleName( roles.getRoleName() );
        roleResponse.permissions( permissionsSetToPermissionResponseSet( roles.getPermissions() ) );

        return roleResponse.build();
    }
}
