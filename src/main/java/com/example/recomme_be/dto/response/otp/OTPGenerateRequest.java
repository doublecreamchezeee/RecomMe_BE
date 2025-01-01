package com.example.recomme_be.dto.response.otp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OTPGenerateRequest {
    @NotBlank(message = "Email is required")
    @NotNull(message = "Email is required")
    private String email;
}
