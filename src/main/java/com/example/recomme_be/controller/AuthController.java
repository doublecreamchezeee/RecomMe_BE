package com.example.recomme_be.controller;

import com.example.recomme_be.configuration.core.PublicEndpoint;
import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.dto.request.auth.*;
import com.example.recomme_be.dto.response.auth.*;
import com.example.recomme_be.service.AuthService;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PublicEndpoint
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody final UserCreationRequest userCreationRequest) {
        authService.create(userCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PublicEndpoint
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenSuccessResponse> login(@Valid @RequestBody final UserLoginRequest userLoginRequest, HttpServletResponse response) {
        final var tokenResponse = authService.login(userLoginRequest);

        String refreshToken = tokenResponse.getRefreshToken();

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(refreshTokenCookie);


        return ResponseEntity.ok(tokenResponse);
    }

    @PublicEndpoint
    @PostMapping(value = "/login/google")
    public ResponseEntity<FirebaseGoogleSignInResponse> loginWithGoogle(@RequestBody final Map<String, String> body) throws FirebaseAuthException {
        String idToken = body.get("idToken");
        FirebaseGoogleSignInResponse response = authService.loginWithGoogle(idToken);
        return ResponseEntity.ok(response);
    }


    @PublicEndpoint
    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenSuccessResponse> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        RefreshTokenSuccessResponse refreshTokenResponse = authService.refreshAccessToken(refreshToken);
        TokenSuccessResponse response = TokenSuccessResponse.builder()
                .accessToken(refreshTokenResponse.getId_token())
                .refreshToken(refreshTokenResponse.getRefresh_token())
                .build();
        return ResponseEntity.ok(response);
    }

    @PublicEndpoint
    @PostMapping("/validateToken")
    public ResponseEntity<ValidatedTokenResponse> validateToken(@RequestBody String token) {
        ValidatedTokenResponse response = authService.validateToken(token);
        return ResponseEntity.ok(response);
    }

    @PublicEndpoint
    @PostMapping("forgotPassword")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        return  authService.forgotPassword(request) ? ResponseEntity.ok(ApiResponse.builder().message("Password updated").build())
                 : ResponseEntity.status(400).body(ApiResponse.builder().message("Invalid email or otp code").code(400).build());
    }

    @PublicEndpoint
    @PostMapping("activateAccount")
    public ResponseEntity<ApiResponse> activateAccount(@RequestBody @Valid ActivateAccountRequest request) {

        return authService.activateAccount(request) ? ResponseEntity.ok(ApiResponse.builder().message("Account activated").build())
                : ResponseEntity.status(400).body(ApiResponse.builder().message("Invalid email or otp code").code(400).build());
    }

}
