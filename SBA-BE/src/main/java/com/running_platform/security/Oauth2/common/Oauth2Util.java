package com.running_platform.security.Oauth2.common;

import com.running_platform.security.Oauth2.factory.Oauth2UserInfoFactoryProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import java.util.Map;

public class Oauth2Util {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";

    // UI-App/Web-Client will use this param to redirect flow to appropriate page
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final String ORIGINAL_REQUEST_URI_PARAM_COOKIE_NAME = "original_request_uri";
    public static final String REDIRECT_SUCCESS_URI_PARAM_COOKIE_NAME = "http://localhost:5173/oauth2/redirect";

}
