package com.running_platform.controller;

import com.running_platform.dto.request.IntrospectToken;
import com.running_platform.dto.request.ResetPasswordRequest;
import com.running_platform.dto.request.SignInRequest;
import com.running_platform.dto.request.UserRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.SignInResponse;
import com.running_platform.dto.response.TokenResponse;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.entity.UserAuth.VerificationTokens;
import com.running_platform.enums.DeviceType;
import com.running_platform.enums.TokenType;
import com.running_platform.repository.UserRepository;
import com.running_platform.repository.VerificationTokenRepository;
import com.running_platform.service.AuthenticationService;
import com.running_platform.service.impl.JwtServiceImp;
import com.running_platform.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j(topic = "AUTHCONTROLLER")
@RequestMapping(("/auth"))
@Tag(name = "Auth Controller")
public class AuthController {
    AuthenticationService authenticationService;
    UserServiceImpl userService;
    JwtServiceImp jwtService;
    VerificationTokenRepository tokenRepository;
    UserRepository userRepository;

    @GetMapping("/verify")
    public void verifyAccount(@RequestParam String token,
                              HttpServletResponse response) throws IOException {

        VerificationTokens verificationToken =
                tokenRepository.findByToken(token);

        if (verificationToken == null) {
            response.sendRedirect("http://localhost:5173/login");
        }
        Users user = verificationToken.getUser();
        user.setEmailVerified(true);

        userRepository.save(user);

        response.sendRedirect("http://localhost:5173/login");

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        log.info("[BUSINESS] Password reset email sent successfully to: {}", email);
        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .code(200)
                        .status("OK")
                        .message("If the email exists, a reset link has been sent")
                        .build());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .code(200)
                        .status("OK")
                        .message("Password reset successfully")
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> signIn(@RequestHeader("Device-Type") DeviceType deviceType, @RequestBody SignInRequest signInRequest) {
        TokenResponse tokenResponse = authenticationService.getAccessToken(signInRequest);
        if(DeviceType.APP.equals(deviceType)) {
            return ResponseEntity.ok()
                    .body(ApiResponse.<Object>builder()
                            .code(200)
                            .status("OK")
                            .message("User logged in successfully")
                            .data(SignInResponse.builder().tokenResponse(tokenResponse).userResponse(userService.getUserByUsername(signInRequest.getUsername())))
                            .build());
        }
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
    public ResponseEntity<ApiResponse<Object>> register(@Valid @RequestBody UserRequest userRegister) {
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
