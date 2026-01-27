package com.laundry.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomHeader {
    USER_INFO("X-User-Info"),
    ROLE_CODE("X-Role-Code"),
    USER_ID("X-User-Id");

    private final String headerName;
}
