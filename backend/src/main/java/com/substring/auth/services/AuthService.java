package com.substring.auth.services;

import com.substring.auth.dtos.UserDto;

public interface AuthService {

	UserDto registerUser(UserDto userDto);

    void forgotPassword(String identifier, String ipAddress);

    void resetPassword(
            String token,
            String otp,
            String newPassword,
            String ipAddress
    );

    void sendInitialOtp(String token);

    void resendOtp(String token);

}
