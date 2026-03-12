package com.example.oauth2.security.Oauth2.common;

public class SecurityEnums {
    public enum AuthProviderId {
        app_custom_authentication, google, facebook, github,
    }
    public enum TokenType {
        access_token, refresh_token
    }
}
