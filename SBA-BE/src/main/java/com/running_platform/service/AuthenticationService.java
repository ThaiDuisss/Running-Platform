package com.running_platform.service;

import com.running_platform.dto.request.SignInRequest;
import com.running_platform.dto.response.TokenResponse;


public interface AuthenticationService {
 TokenResponse getAccessToken(SignInRequest signInRequest);
 TokenResponse getRefreshToken(String token);
}



