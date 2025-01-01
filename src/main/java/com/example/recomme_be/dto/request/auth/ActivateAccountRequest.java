package com.example.recomme_be.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivateAccountRequest {
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email is required")
    private String email;
    @NotNull(message = "OTP Code is required")
    @NotBlank(message = "OTP Code is required")
    private String otpCode;

}
