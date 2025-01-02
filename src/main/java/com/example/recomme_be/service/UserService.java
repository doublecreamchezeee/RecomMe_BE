package com.example.recomme_be.service;

import com.example.recomme_be.client.FirebaseUserClient;
import com.example.recomme_be.dto.request.auth.UserUpdateRequest;
import com.example.recomme_be.dto.response.auth.UserInfoResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final FirebaseUserClient firebaseUserClient;

    public void updateByEmail(@NonNull String email, @NonNull UserUpdateRequest userUpdateRequest) {
        firebaseUserClient.updateByEmail(email, userUpdateRequest);
    }

    public void updateByUid(@NonNull String uid, @NonNull UserUpdateRequest userUpdateRequest) {
       firebaseUserClient.updateByUid(uid, userUpdateRequest);
    }

    public UserInfoResponse getUserInfo(String userId, String email) {
        return  firebaseUserClient.getUserInfo(userId, email);
    }

}
