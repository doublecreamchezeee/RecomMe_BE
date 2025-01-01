package com.example.recomme_be.client;

import com.example.recomme_be.dto.request.auth.UserCreationRequest;
import com.example.recomme_be.dto.request.auth.UserLoginRequest;
import com.example.recomme_be.dto.request.auth.UserUpdateRequest;
import com.example.recomme_be.dto.response.auth.*;
import com.example.recomme_be.exception.AccountAlreadyExistsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseUserClient {

    private final FirebaseAuth firebaseAuth;


    @SneakyThrows
    public void create(@NonNull final UserCreationRequest userCreationRequest) {
        log.info("Creating user with email: {}", userCreationRequest.getEmail());
        final var request = new UserRecord.CreateRequest()
                .setEmail(userCreationRequest.getEmail())
                .setPassword(userCreationRequest.getPassword())
                .setEmailVerified(Boolean.TRUE)
                .setDisplayName(userCreationRequest.getDisplayName());
        //.setPhoneNumber(userCreationRequest.getPhoneNumber());
        //.setPhotoUrl(userCreationRequest.getPhotoUrl());

        /*if (!Strings.isNullOrEmpty(userCreationRequest.getPhotoUrl())) {
            request.setPhotoUrl(userCreationRequest.getPhotoUrl());
        }*/

        try {
            firebaseAuth.createUser(request);
            log.info("User successfully created: {}", userCreationRequest.getEmail());
        } catch (final FirebaseAuthException exception) {
            if (exception.getMessage().contains("EMAIL_EXISTS")) {
                throw new AccountAlreadyExistsException("Account with provided email already exists");
            }
            if (exception.getMessage().contains("PHONE_NUMBER_EXISTS")) {
                throw new AccountAlreadyExistsException("Account with provided phone number already exists");
            }
            throw new RuntimeException("Error creating user: " + exception.getMessage(), exception);
        }
    }

    public void updateByEmail(@NonNull String email, @NonNull UserUpdateRequest userUpdateRequest) {
        try {
            UserRecord userRecord = firebaseAuth.getUserByEmail(email);

            updateByUid(userRecord.getUid(), userUpdateRequest);
        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Error finding user by email: " + e.getMessage(), e);
        }
    }

    public void updateByUid(@NonNull String uid, @NonNull UserUpdateRequest userUpdateRequest) {
        final var request = new UserRecord.UpdateRequest(uid);

        if (userUpdateRequest.getEmail() != null) {
            request.setEmail(userUpdateRequest.getEmail());
        }
        if (userUpdateRequest.getDisplayName() != null) {
            request.setDisplayName(userUpdateRequest.getDisplayName());
        }
        /*if (userUpdateRequest.getPhoneNumber() != null) {
            request.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        }
        if (userUpdateRequest.getPhotoUrl() != null
                && !Strings.isNullOrEmpty(userUpdateRequest.getPhotoUrl())) {
            request.setPhotoUrl(userUpdateRequest.getPhotoUrl());
        }*/
        if (userUpdateRequest.getPassword() != null) {
            request.setPassword(userUpdateRequest.getPassword());
        }
        if (userUpdateRequest.getDisabled() != null) {
            request.setDisabled(userUpdateRequest.getDisabled());
        }

        try {
            firebaseAuth.updateUser(request);
        } catch (final Exception exception) {
            throw new RuntimeException("Error updating user: " + exception.getMessage(), exception);
        }
    }

    public UserInfoResponse getUserInfo(String userId, String email) {
        try {
            UserRecord userRecord;
            if (userId != null) {
                userRecord = firebaseAuth.getUser(userId);
            } else if (email != null) {
                userRecord = firebaseAuth.getUserByEmail(email);
            } else {
                throw new IllegalArgumentException("Either userId or email must be provided.");
            }

            return UserInfoResponse.builder()
                    .uid(userRecord.getUid())
                    .email(userRecord.getEmail())
                    .displayName(userRecord.getDisplayName())
                    .emailVerified(userRecord.isEmailVerified())
                    .phoneNumber(userRecord.getPhoneNumber())
                    .photoUrl(userRecord.getPhotoUrl())
                    .build();

        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Error retrieving user information: " + e.getMessage(), e);
        }
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
}
