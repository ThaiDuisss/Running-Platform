package com.example.oauth2.security.Oauth2;

import com.example.oauth2.entity.UserEntity;
import com.example.oauth2.security.AppSecurityUtils;
import com.example.oauth2.security.CustomUserDetails;
import com.example.oauth2.security.Oauth2.common.CustomAbstractOAuth2UserInfo;
import com.example.oauth2.security.Oauth2.common.Oauth2Util;
import com.example.oauth2.security.Oauth2.common.SecurityEnums;
import com.example.oauth2.service.webapp.user.UserMapper;
import com.example.oauth2.service.webapp.user.UserService;
import com.example.oauth2.service.webapp.user.dto.UserDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j(topic = "CUSTOMOAUTH2USERSERVICE")
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    UserService userService;
    UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("Vao day roi");
        try {
            return processOauth2User(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOauth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        // Mapped OAuth2User to specific CustomAbstractOAuth2UserInfo for that registration id
        // clientRegistrationId - (google, facebook, gitHub, or Custom Auth Provider - ( keyClock, okta, authServer etc.)
        log.info("vao process roi");
        String clientRegistrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        CustomAbstractOAuth2UserInfo abstractOAuth2UserInfo = Oauth2Util.getOAuth2UserInfo(clientRegistrationId, oAuth2User.getAttributes());
        // Check if the email is provided by the OAuthProvider
        SecurityEnums.AuthProviderId registeredProviderId = SecurityEnums.AuthProviderId.valueOf(clientRegistrationId);
        String userEmail = abstractOAuth2UserInfo.getEmail();
        log.info(userEmail);

        if (userEmail.isEmpty()) {
            throw new InternalAuthenticationServiceException("Sorry, Couldn't retrieve your email from Provider " + clientRegistrationId + ". Email not available or Private by default");
        }
        Optional<UserDTO> userFindByEmail = userService.findOptionalUserByEmail(userEmail);
        if (userFindByEmail.isEmpty()) {
            userFindByEmail = Optional.of(registerNewOAuthUser(oAuth2UserRequest, abstractOAuth2UserInfo));
        }
        UserDTO userDTO = userFindByEmail.get();

        if(userDTO.getRegisteredProviderName().equals(registeredProviderId)) {
            updateExistingOAuthUser(userDTO, abstractOAuth2UserInfo);
        }
        else {
            String incorrectProviderChoice = "Sorry, this email is linked with \"" + userDTO.getRegisteredProviderName() + "\" account. " +
                    "Please use your \"" + userDTO.getRegisteredProviderName() + "\" account to login.";
            throw new InternalAuthenticationServiceException(incorrectProviderChoice);
        }

        List<GrantedAuthority> role = new ArrayList<>(oAuth2User.getAuthorities());
        role.add(new SimpleGrantedAuthority(AppSecurityUtils.ROLE_DEFAULT));
        UserEntity user = userMapper.toResponse(userDTO);
        return CustomUserDetails.buildWithAuthAttributesAndAuthorities(user, role, oAuth2User.getAttributes());
    }

    private UserDTO registerNewOAuthUser(OAuth2UserRequest oAuth2UserRequest,
                                         CustomAbstractOAuth2UserInfo customAbstractOAuth2UserInfo) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(customAbstractOAuth2UserInfo.getName());
        userDTO.setEmail(customAbstractOAuth2UserInfo.getEmail());
        userDTO.setRegisteredProviderName(SecurityEnums.AuthProviderId.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        userDTO.setRegisteredProviderId(customAbstractOAuth2UserInfo.getId());
        userDTO.setRoles(Set.of(AppSecurityUtils.ROLE_DEFAULT));
        userDTO.setEmailVerified(true);
        return  userService.createUser(userDTO);
    }

    private void updateExistingOAuthUser(UserDTO existingUserDTO,
                                         CustomAbstractOAuth2UserInfo customAbstractOAuth2UserInfo) {
        existingUserDTO.setFullName(customAbstractOAuth2UserInfo.getName());
        existingUserDTO.setImageUrl(customAbstractOAuth2UserInfo.getImageUrl());
        UserDTO updatedUserDTO = userService.updateUser(existingUserDTO);
        BeanUtils.copyProperties(updatedUserDTO, existingUserDTO);
    }
}
