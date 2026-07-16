package com.substring.auth.services;

import com.substring.auth.dtos.UserDto;

public interface AuthService {

	UserDto registerUser(UserDto userDto);

	void resetPassword(String token, String otp, String newPassword, String ipAddress);

	void forgotPassword(String identiFier, String ipAddress);

	void sendOtp(String token);

}
