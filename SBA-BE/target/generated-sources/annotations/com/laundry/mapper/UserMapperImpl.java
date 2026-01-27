package com.laundry.mapper;

import com.laundry.constant.RoleEnum;
import com.laundry.dto.request.UserProfile;
import com.laundry.dto.request.UserRegister;
import com.laundry.dto.response.ProfileResponse;
import com.laundry.dto.response.RoleResponse;
import com.laundry.dto.response.UserResponse;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-27T14:59:35+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

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
