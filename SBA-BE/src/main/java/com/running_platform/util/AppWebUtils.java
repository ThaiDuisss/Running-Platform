package com.running_platform.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

public class AppWebUtils {
    private static int cookieExpireSeconds;


    public AppWebUtils(int cookieExpireSeconds) {
        AppWebUtils.cookieExpireSeconds = cookieExpireSeconds;
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieKey) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieKey)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void createCookie(HttpServletResponse response,
                                    String cookieKey,
                                    Long expireTimeCookie,
                                    String cookieValue) {

        ResponseCookie cookie = ResponseCookie.from(cookieKey, cookieValue)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expireTimeCookie != 0 ? expireTimeCookie : cookieExpireSeconds)
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void deleteCookie(HttpServletResponse response,
                                    HttpServletRequest request,
                                    String keyName) {

        ResponseCookie cookie = ResponseCookie.from(keyName, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

}
