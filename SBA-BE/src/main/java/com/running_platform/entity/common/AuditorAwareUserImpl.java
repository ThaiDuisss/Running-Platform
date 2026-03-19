package com.running_platform.entity.common;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.exception.AppException;
import com.running_platform.repository.UserRepository;
import com.running_platform.security.AppSecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component("auditorAwareUserImpl")
@RequiredArgsConstructor
public class AuditorAwareUserImpl implements AuditorAware<Users> {
    private final UserRepository userRepository;

    @Override
    public Optional<Users> getCurrentAuditor() {
        Optional<Long> optionalUserId = Optional.ofNullable(AppSecurityUtils.getCurrentUserPrinciple())
                .map(customUserDetails -> customUserDetails.getUserEntity())
                .map(userEntity -> userEntity.getId());
        return optionalUserId.map(e -> userRepository.findById(e).orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR)));
    }
}
