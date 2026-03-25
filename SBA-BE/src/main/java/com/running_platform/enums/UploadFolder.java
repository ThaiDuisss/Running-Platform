package com.running_platform.enums;

public enum UploadFolder {
    AVATAR_USER("running_platform/users/avatars"),
    ARTICLES("running_platform/articles"),
    HIGHLIGHT_ROUTE("running_platform/highlight_routes")
    ;

    private final String path;

    UploadFolder(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
