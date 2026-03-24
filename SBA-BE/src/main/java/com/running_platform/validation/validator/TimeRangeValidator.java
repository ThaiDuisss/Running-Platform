package com.running_platform.validation.validator;

import com.running_platform.dto.request.challenge.AdminCreateChallengeRequest;
import com.running_platform.validation.annotation.ValidTimeRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TimeRangeValidator
        implements ConstraintValidator<ValidTimeRange, AdminCreateChallengeRequest> {

    @Override
    public boolean isValid(AdminCreateChallengeRequest request,
                           ConstraintValidatorContext context) {

        // nếu null → để @NotNull xử lý
        if (request.getStartTime() == null || request.getEndTime() == null) {
            return true;
        }

        return request.getStartTime().isBefore(request.getEndTime());
    }
}
