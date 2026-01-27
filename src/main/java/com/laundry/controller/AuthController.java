package com.laundry.controller;

import com.laundry.dto.DTO.IntrospectToken;
import com.laundry.dto.response.ApiResponse;
import com.laundry.dto.request.SignInRequest;
import com.laundry.dto.request.UserRegister;
import com.laundry.dto.response.TokenResponse;
import com.laundry.enums.TokenType;
import com.laundry.service.AuthenticationService;
import com.laundry.service.impl.JwtServiceImp;
import com.laundry.service.impl.UserServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j(topic = "AUTHCONTROLLER")
public class AuthController {
    AuthenticationService authenticationService;
    UserServiceImpl userService;
    JwtServiceImp jwtService;

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<Object>> signIn(@RequestBody SignInRequest signInRequest) {
        TokenResponse tokenResponse = authenticationService.getAccessToken(signInRequest);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(60 * 60 * 24 * 7L)
                .build();
        log.info("[BUSINESS] User logged in successfully: {}", signInRequest.getUsername());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.<Object>builder()
                        .code(200)
                        .status("OK")
                        .message("User logged in successfully")
                        .data(tokenResponse.getAccessToken())
                        .build());
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody UserRegister userRegister) {
        return ResponseEntity.ok()
                .body(ApiResponse.<Object>builder()
                        .code(200)
                        .status("OK")
                        .message("User register in successfully")
                        .data(userService.register(userRegister))
                        .build());
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<ApiResponse<Object>> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {
        TokenResponse tokenResponse = authenticationService.getRefreshToken(refreshToken);

        log.info("Access token refreshed successfully");
        return ResponseEntity.ok()
                .body(ApiResponse.<Object>builder()
                        .code(200)
                        .status("OK")
                        .message("Access token refreshed successfully")
                        .data(tokenResponse.getAccessToken())
                        .build());
    }

    @GetMapping("/mail-again/{email}")
    public ResponseEntity<ApiResponse<?>> mailAgain(@PathVariable("email") String email) {
        userService.sendEmail(email);
        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .code(200)
                        .status("OK")
                        .message("Email sent successfully")
                        .build());
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout() {
            ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("None")
                    .maxAge(0)
                    .build();

        log.info("User logged out successfully");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(ApiResponse.<Object>builder()
                        .code(200)
                        .status("OK")
                        .message("User logged out successfully")
                        .build());
    }
    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<Long>> getInfoFromToken(@RequestBody IntrospectToken accessToken) {
        return ResponseEntity.ok()
                .body(ApiResponse.<Long>builder()
                        .code(200)
                        .status("OK")
                        .message("Get info from token successfully")
                        .data(jwtService.extractToken(accessToken.getToken(), TokenType.ACCESSTOKEN))
                        .build());
    }
}
