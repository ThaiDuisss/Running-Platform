package com.running_platform.security.Oauth2.common;



import com.running_platform.util.AppUtils;
import com.running_platform.util.AppWebUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static com.running_platform.security.Oauth2.common.Oauth2Util.*;


@Component
public class HttpCookieOauth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "[OUATH2 Request] request cannot be null");
         return AppWebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                 .map(cookie -> deserializeCookie(cookie))
                 .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {

        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");

        if(authorizationRequest == null) {
           removeAuthorizationRequestCookies(request, response);
           return;
        }

        AppWebUtils.createCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, 0L, AppUtils.serialize(authorizationRequest));
        AppWebUtils.createCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, 0L, request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME));
        AppWebUtils.createCookie(response,  ORIGINAL_REQUEST_URI_PARAM_COOKIE_NAME, 0L, request.getParameter(ORIGINAL_REQUEST_URI_PARAM_COOKIE_NAME));

    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest originalRequest = loadAuthorizationRequest(request);
        removeAuthorizationRequestCookies(request, response);
        return originalRequest;
    }

    private OAuth2AuthorizationRequest deserializeCookie (Cookie cookie) {
        return AppUtils.deserialize(cookie.getValue());
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request,
                                                  HttpServletResponse response) {
        AppWebUtils.deleteCookie(response, request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        AppWebUtils.deleteCookie(response, request, REDIRECT_URI_PARAM_COOKIE_NAME);
        AppWebUtils.deleteCookie(response, request, ORIGINAL_REQUEST_URI_PARAM_COOKIE_NAME);
    }



}
