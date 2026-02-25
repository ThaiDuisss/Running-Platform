package com.laundry.config;


import com.laundry.constant.RoleEnum;
import com.laundry.entity.UserAuth.Roles;
import com.laundry.entity.UserAuth.Users;
import com.laundry.repository.AuthRepository;
import com.laundry.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
public class ApplicationInitConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner initApplicationRunner(AuthRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUsername(("ADMIN")).isEmpty()) {
                Set<Roles> roles = new HashSet<Roles>();
                Roles adminRole = roleRepository.findByRoleName(RoleEnum.ADMIN);
                if (adminRole == null) {
                    adminRole = Roles.builder().roleName(RoleEnum.ADMIN).build();
                    roleRepository.save(adminRole);
                }
                roles.add(adminRole);
                Users user = Users.builder().username("ADMIN")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .emailVerified(true)
                        .build();
                userRepository.save(user);
                log.warn("Initialized admin user with username: ADMIN and password: admin");
            }
        };
    }
}
