package com.example.recomme_be.dto.response.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirebaseSignInResponse {
    private String idToken;
    private String refreshToken;
//    private String localId;
//    private String email;
}
