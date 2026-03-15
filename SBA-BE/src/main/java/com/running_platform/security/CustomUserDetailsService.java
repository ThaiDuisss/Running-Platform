package com.running_platform.security;


import com.running_platform.constant.ErrorEnum;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.exception.AppException;
import com.running_platform.repository.UserRepository;
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
        Users userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
        return CustomUserDetails.buildFromUserEntity(userEntity);
    }

    public UserDetails loadUserById(Long id) {
        Users user =
                userRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
        return CustomUserDetails.buildFromUserEntity(user);
    }
}
