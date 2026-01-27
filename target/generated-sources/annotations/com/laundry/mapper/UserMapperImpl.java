package com.laundry.mapper;

import com.laundry.dto.request.UserProfile;
import com.laundry.dto.request.UserRegister;
import com.laundry.dto.request.UserRequest;
import com.laundry.dto.response.PermissionResponse;
import com.laundry.dto.response.ProfileResponse;
import com.laundry.dto.response.RoleResponse;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.Permissions;
import com.laundry.entity.Roles;
import com.laundry.entity.Users;
import com.laundry.enums.RoleEnum;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-03T21:25:08+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public Users toEntity(UserRequest request) {
        if ( request == null ) {
            return null;
        }

        Users.UsersBuilder users = Users.builder();

        users.username( request.getUsername() );
        users.password( request.getPassword() );
        users.roles( roleEnumSetToRolesSet( request.getRoles() ) );

        return users.build();
    }

    @Override
    public UserResponse toResponse(Users entity) {
        if ( entity == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( entity.getId() );
        userResponse.username( entity.getUsername() );
        userResponse.emailVerified( entity.isEmailVerified() );
        userResponse.roles( rolesSetToRoleResponseSet( entity.getRoles() ) );

        return userResponse.build();
    }

    @Override
    public List<UserResponse> toResponseList(List<Users> entities) {
        if ( entities == null ) {
            return null;
        }

        List<UserResponse> list = new ArrayList<UserResponse>( entities.size() );
        for ( Users users : entities ) {
            list.add( toResponse( users ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(UserRequest request, Users entity) {
        if ( request == null ) {
            return;
        }

        entity.setUsername( request.getUsername() );
        entity.setPassword( request.getPassword() );
        if ( entity.getRoles() != null ) {
            Set<Roles> set = roleEnumSetToRolesSet( request.getRoles() );
            if ( set != null ) {
                entity.getRoles().clear();
                entity.getRoles().addAll( set );
            }
            else {
                entity.setRoles( null );
            }
        }
        else {
            Set<Roles> set = roleEnumSetToRolesSet( request.getRoles() );
            if ( set != null ) {
                entity.setRoles( set );
            }
        }
    }

    @Override
    public UserProfile toUserProfile(UserRegister userRegister) {
        if ( userRegister == null ) {
            return null;
        }

        UserProfile userProfile = new UserProfile();

        userProfile.setFullName( userRegister.getFullName() );
        userProfile.setPhoneNumber( userRegister.getPhoneNumber() );

        return userProfile;
    }

    @Override
    public UserResponse toUserResponse(UserRegister user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.username( user.getUsername() );
        userResponse.roles( roleEnumSetToRoleResponseSet( user.getRoles() ) );

        return userResponse.build();
    }

    @Override
    public ProfileResponse toProfileResponse(UserProfile user) {
        if ( user == null ) {
            return null;
        }

        ProfileResponse.ProfileResponseBuilder profileResponse = ProfileResponse.builder();

        profileResponse.userId( user.getUserId() );
        profileResponse.fullName( user.getFullName() );
        profileResponse.phoneNumber( user.getPhoneNumber() );
        profileResponse.avatar( user.getAvatar() );

        return profileResponse.build();
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

    protected RoleResponse roleEnumToRoleResponse(RoleEnum roleEnum) {
        if ( roleEnum == null ) {
            return null;
        }

        RoleResponse.RoleResponseBuilder roleResponse = RoleResponse.builder();

        return roleResponse.build();
    }

    protected Set<RoleResponse> roleEnumSetToRoleResponseSet(Set<RoleEnum> set) {
        if ( set == null ) {
            return null;
        }

        Set<RoleResponse> set1 = new LinkedHashSet<RoleResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( RoleEnum roleEnum : set ) {
            set1.add( roleEnumToRoleResponse( roleEnum ) );
        }

        return set1;
    }
}
