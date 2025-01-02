package com.example.recomme_be.utility;

import java.security.SecureRandom;

public class OTPUtil {
    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 6;
    private OTPUtil(){}
    public static String generateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
