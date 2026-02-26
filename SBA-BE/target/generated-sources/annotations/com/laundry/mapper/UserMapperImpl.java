package com.laundry.mapper;

import com.laundry.dto.request.UserRequest;
import com.laundry.dto.response.PermissionResponse;
import com.laundry.dto.response.RoleResponse;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.UserAuth.Permissions;
import com.laundry.entity.UserAuth.Roles;
import com.laundry.entity.UserAuth.Users;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-26T08:33:19+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public Users toUser(UserRequest users) {
        if ( users == null ) {
            return null;
        }

        Users.UsersBuilder<?, ?> users1 = Users.builder();

        users1.username( users.getUsername() );
        users1.password( users.getPassword() );
        users1.phoneNumber( users.getPhoneNumber() );

        return users1.build();
    }

    @Override
    public UserResponse toUserResponse(Users user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.username( user.getUsername() );
        userResponse.emailVerified( user.isEmailVerified() );
        userResponse.roles( rolesSetToRoleResponseSet( user.getRoles() ) );
        userResponse.location( user.getLocation() );
        userResponse.latitude( user.getLatitude() );
        userResponse.longitude( user.getLongitude() );
        userResponse.avatar( user.getAvatar() );
        userResponse.vipExpiredAt( user.getVipExpiredAt() );
        userResponse.phoneNumber( user.getPhoneNumber() );

        return userResponse.build();
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

    protected Set<RoleResponse> rolesSetToRoleResponseSet(Set<Roles> set) {
        if ( set == null ) {
            return null;
        }

        Set<RoleResponse> set1 = new LinkedHashSet<RoleResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Roles roles : set ) {
            set1.add( rolesToRoleResponse( roles ) );
        }

        return set1;
    }
}
