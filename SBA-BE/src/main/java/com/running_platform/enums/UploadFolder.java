package com.running_platform.enums;

public enum UploadFolder {
    AVATAR_USER("running_platform/users/avatars");

    private final String path;

    UploadFolder(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
