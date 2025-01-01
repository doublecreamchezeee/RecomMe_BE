package com.example.recomme_be.controller;

import com.example.recomme_be.configuration.core.PublicEndpoint;
import com.example.recomme_be.dto.response.otp.OTPGenerateRequest;
import com.example.recomme_be.service.OTPService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/otp")
public class OTPController {
    private final OTPService otpService;
    @PublicEndpoint
    @PostMapping("/generate")
    public ResponseEntity<String> generateOTP(@RequestBody @Valid OTPGenerateRequest request) {

        otpService.generateAndSendOTP(request.getEmail());
        return ResponseEntity.ok("OTP sent to " + request.getEmail());
    }
}
