package com.running_platform.security.Oauth2;

import com.running_platform.config.AppProperties;
import com.running_platform.constant.ErrorEnum;
import com.running_platform.exception.AppException;
import com.running_platform.security.JwtTokenProvider;
import com.running_platform.security.Oauth2.common.HttpCookieOauth2AuthorizationRequestRepository;
import com.running_platform.util.AppWebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import static com.running_platform.security.Oauth2.common.Oauth2Util.ORIGINAL_REQUEST_URI_PARAM_COOKIE_NAME;
import static com.running_platform.security.Oauth2.common.Oauth2Util.REDIRECT_SUCCESS_URI_PARAM_COOKIE_NAME;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j(topic = "OAUTH2_SUCCESS")
public class Oauth2AuthenticationSuccessZHandler extends SimpleUrlAuthenticationSuccessHandler {
    HttpCookieOauth2AuthorizationRequestRepository httpCookieOauth2AuthorizationRequestRepository;
    JwtTokenProvider tokenProvider;
    AppProperties appProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String uriRedirect = determineTargetUrl(request, response, authentication);
        if(response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + uriRedirect);
            return;
        }
        removeInformationInCookie(request, response);

        getRedirectStrategy().sendRedirect(request, response, uriRedirect);
    }

    public String determineTargetUrl (HttpServletRequest request, HttpServletResponse response, Authentication  authentication) {
        Optional<String> redirectUri = Optional.of(REDIRECT_SUCCESS_URI_PARAM_COOKIE_NAME);
//                AppWebUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);
        Optional<String> originalRequestUri = AppWebUtils.getCookie(request, ORIGINAL_REQUEST_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        if(redirectUri.isPresent() && !isRedirectOriginAuthorized(redirectUri.get())) {
            throw new AppException(ErrorEnum.FAILED_OAUTH2_REDIRECT);
        }

        String uri = redirectUri.orElse(getDefaultTargetUrl());
        String jwtToken = tokenProvider.generateAccessToken(authentication);

        return UriComponentsBuilder.fromUriString(uri)
                .queryParam("token", jwtToken)
                .queryParam(ORIGINAL_REQUEST_URI_PARAM_COOKIE_NAME, originalRequestUri)
                .build().toString();
    }

    public void removeInformationInCookie(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOauth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    public boolean isRedirectOriginAuthorized (String uri) {
        URI clientRedirectUri = URI.create(uri);
        return Arrays.stream(appProperties.getOAuth2().getAuthorizedRedirectOrigins())
                .anyMatch(authorizedRedirectOrigin -> {
                    URI authorizedURI = URI.create(authorizedRedirectOrigin);
                    if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }

}
