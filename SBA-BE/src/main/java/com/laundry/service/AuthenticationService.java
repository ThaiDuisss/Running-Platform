package com.laundry.service;

import com.laundry.dto.request.SignInRequest;
import com.laundry.dto.response.TokenResponse;


public interface AuthenticationService {
 TokenResponse getAccessToken(SignInRequest signInRequest);
 TokenResponse getRefreshToken(String token);
}



