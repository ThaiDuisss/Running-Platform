package com.laundry.mapper;

import com.laundry.constant.RoleEnum;
import com.laundry.dto.request.AdminCreateUserRequest;
import com.laundry.dto.request.AdminUpdateUserRequest;
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
    date = "2026-02-25T20:19:34+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public Users toUserProfile(UserRequest users) {
        if ( users == null ) {
            return null;
        }

        Users.UsersBuilder users1 = Users.builder();

        users1.username( users.getUsername() );
        users1.password( users.getPassword() );
        users1.roles( roleEnumSetToRolesSet( users.getRoles() ) );
        users1.phoneNumber( users.getPhoneNumber() );

        return users1.build();
    }

    @Override
    public UserResponse toUserResponse(Users user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
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

    @Override
    public Users toUser(AdminCreateUserRequest req) {
        if ( req == null ) {
            return null;
        }

        Users.UsersBuilder users = Users.builder();

        users.username( req.getUsername() );
        users.password( req.getPassword() );
        users.roles( roleEnumSetToRolesSet( req.getRoles() ) );
        users.phoneNumber( req.getPhoneNumber() );

        return users.build();
    }

    @Override
    public void updateUser(Users user, AdminUpdateUserRequest req) {
        if ( req == null ) {
            return;
        }

        user.setUsername( req.getUsername() );
        if ( req.getEmailVerified() != null ) {
            user.setEmailVerified( req.getEmailVerified() );
        }
        user.setPhoneNumber( req.getPhoneNumber() );
    }

    protected Roles roleEnumToRoles(RoleEnum roleEnum) {
        if ( roleEnum == null ) {
            return null;
        }

        Roles.RolesBuilder roles = Roles.builder();

        return roles.build();
    }

    protected Set<Roles> roleEnumSetToRolesSet(Set<RoleEnum> set) {
        if ( set == null ) {
            return null;
        }

        Set<Roles> set1 = new LinkedHashSet<Roles>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( RoleEnum roleEnum : set ) {
            set1.add( roleEnumToRoles( roleEnum ) );
        }

        return set1;
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
