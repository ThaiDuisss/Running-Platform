package com.running_platform.security.Oauth2.common;

public class SecurityEnums {
    public enum AuthProviderId {
        app_custom_authentication, google, facebook, github,
    }
    public enum TokenType {
        access_token, refresh_token
    }
}
