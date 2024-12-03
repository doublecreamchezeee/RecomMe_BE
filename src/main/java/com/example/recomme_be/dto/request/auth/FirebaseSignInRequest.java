package com.example.recomme_be.dto.request.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirebaseSignInRequest {
    private String email;
    private String password;
    private boolean returnSecureToken;
}
