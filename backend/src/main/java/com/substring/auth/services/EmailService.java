package com.substring.auth.services;

public interface EmailService {

	// Account deletion
	void sendAccountDeletedMail(String email, String name);

	// Forgot password reset link
	void sendResetPasswordMail(String email, String name, String resetLink);

	// OTP for reset password
	void sendOtpMail(String email, String name, String otp);

	// Password changed notification
	void sendPasswordChangedMail(String email, String name);
}
