package com.running_platform.enums;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.exception.AppException;

public enum CategoryEnum {
    TECHNIQUE,
    EQUIPMENT,
    NUTRITION;

    public static CategoryEnum fromString(String category) {
        for (CategoryEnum c : CategoryEnum.values()) {
            if (c.name().equalsIgnoreCase(category)) {
                return c;
            }
        }
        throw new AppException(ErrorEnum.CATEGORY_NOT_FOUND);
    }
}
