package com.example.recomme_be.dto.response.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
    private String uid;
    private String email;
    private String displayName;
    private boolean emailVerified;
    private String phoneNumber;
    private String photoUrl;
}
