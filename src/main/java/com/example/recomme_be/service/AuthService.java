package com.example.recomme_be.service;

import com.example.recomme_be.client.FirebaseAuthClient;
import com.example.recomme_be.client.FirebaseUserClient;
import com.example.recomme_be.dto.request.auth.*;
import com.example.recomme_be.dto.response.auth.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final FirebaseAuthClient firebaseAuthClient;
    private final FirebaseUserClient firebaseUserClient;
    private final OTPService otpService;


    public TokenSuccessResponse login(@NonNull final UserLoginRequest userLoginRequest) {
        return firebaseAuthClient.login(userLoginRequest);
    }
    public FirebaseGoogleSignInResponse loginWithGoogle(@NonNull final String idToken) throws FirebaseAuthException {
        FirebaseToken firebaseToken = firebaseAuthClient.verifyToken(idToken);

        return FirebaseGoogleSignInResponse.builder()
                .uid(firebaseToken.getUid())
                .email(firebaseToken.getEmail())
                .build();
    }

    public RefreshTokenSuccessResponse refreshAccessToken(@NonNull final String refreshToken) {
        return firebaseAuthClient.refreshAccessToken(refreshToken);
    }



    public ValidatedTokenResponse validateToken(@NonNull final String token) {
        try {
            FirebaseToken decodeToken = FirebaseAuth.getInstance().verifyIdToken(token);

            return ValidatedTokenResponse.builder()
                    .isValidated(true)
                    .message("Token validation successful.")
                    .build();
        } catch (FirebaseAuthException e) {
            return ValidatedTokenResponse.builder()
                    .isValidated(false)
                    .message("Invalid token: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return ValidatedTokenResponse.builder()
                    .isValidated(false)
                    .message("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    public void create(@NonNull final UserCreationRequest userCreationRequest) {
        firebaseUserClient.create(userCreationRequest);
    }

    public boolean activateAccount(ActivateAccountRequest activateAccountRequest) {
        if (!otpService.validateOTP(activateAccountRequest.getEmail(), activateAccountRequest.getOtpCode())) {
            return false;
        }
        final var request = UserUpdateRequest.builder().disabled(false).build();
        firebaseUserClient.updateByEmail(activateAccountRequest.getEmail(), request);
        return true;
    }

    public boolean forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        if (!otpService.validateOTP(forgotPasswordRequest.getEmail(), forgotPasswordRequest.getOtpCode())) {
            return false;
        }
        final var userUpdateRequest = UserUpdateRequest.builder().password(forgotPasswordRequest.getNewPassword()).build();
        firebaseUserClient.updateByEmail(forgotPasswordRequest.getEmail(), userUpdateRequest);
        return true;
    }
}