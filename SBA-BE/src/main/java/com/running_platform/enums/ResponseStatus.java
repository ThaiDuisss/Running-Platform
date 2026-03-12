package com.running_platform.enums;

public enum ResponseStatus {
    SUCCESS("success"),
    ERROR("error");

    private final String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
