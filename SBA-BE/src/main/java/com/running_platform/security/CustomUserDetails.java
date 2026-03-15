package com.running_platform.security;

import com.running_platform.entity.UserAuth.Users;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class    CustomUserDetails implements UserDetails, OAuth2User {
    String email;
    String password;

    Users userEntity;

    Collection<? extends GrantedAuthority> authorities;

    Map<String, Object> attributes;

    public CustomUserDetails(String email,
                             String password,
                             Users userEntity,
                             Collection<? extends GrantedAuthority> authorities,
                             Map<String, Object> attributes) {
        this.email = email;
        this.password = password;
        this.userEntity = userEntity;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    public static CustomUserDetails buildFromUserEntity(Users userEntity) {
        Collection<? extends GrantedAuthority> grantedAuthorities = AppSecurityUtils
                .convertRolesSetToGrantedAuthorityList(userEntity.getRoles());
        return new CustomUserDetails(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity,
                grantedAuthorities,
                Map.of()
        );
    }

    public static CustomUserDetails buildWithAuthAttributesAndAuthorities(Users userEntity,
                                                                          Collection<? extends GrantedAuthority> authorities,
                                                                          Map<String, Object> attributes) {

        CustomUserDetails customUserDetails = CustomUserDetails.buildFromUserEntity(userEntity);
        customUserDetails.setAuthorities(authorities);
        customUserDetails.setAttributes(attributes);
        return customUserDetails;
    }

    @Override
    public String getName() {
        return String.valueOf(this.getEmail());
    }

    // Oauth2User fields
    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.userEntity.isEmailVerified();
    }
}
