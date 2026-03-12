package com.example.oauth2.security;

import com.example.oauth2.entity.UserEntity;
import com.example.oauth2.repository.UserRepository;
import com.example.oauth2.util.exceptions.AppExceptionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(AppExceptionConstants.BAD_LOGIN_CREDENTIALS));
        return CustomUserDetails.buildFromUserEntity(userEntity);
    }

    public UserDetails loadUserById(Long id) {
        UserEntity user =
                userRepository.findById(id)
                        .orElseThrow(() -> new UsernameNotFoundException(AppExceptionConstants.BAD_LOGIN_CREDENTIALS));
        return CustomUserDetails.buildFromUserEntity(user);
    }
}
