package com.example.recomme_be.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidatedTokenResponse {
    boolean isValidated;
    String message;
}
