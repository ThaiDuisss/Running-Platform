package com.laundry.service;

import com.laundry.repository.AuthRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
public class UserServiceDetail {

   AuthRepository authRepository;

    public UserDetailsService userDetailsService() {
        return username -> authRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found") );
    }
}
