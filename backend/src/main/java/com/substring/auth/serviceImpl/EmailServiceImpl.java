package com.substring.auth.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.substring.auth.services.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;

	public void sendAccountDeletedMail(String email, String name) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(email);
		message.setSubject("Your Account Has Been Deleted Successfully");

		message.setText("Hello " + name + ",\n\n"
				+ "This email confirms that your account associated with this email address has been permanently deleted from our system.\n\n"
				+ "Deletion Time: " + LocalDateTime.now() + "\n\n"
				+ "As part of this process, your profile information, authentication details, and associated account data have been removed according to our data retention policy.\n\n"
				+ "If you initiated this request, no further action is required.\n\n"
				+ "If you did not request this account deletion or believe this action was performed without your authorization, please contact our support team immediately.\n\n"
				+ "Support Email: support@yourdomain.com\n\n" + "Thank you for using our platform.\n\n"
				+ "Best regards,\n" + "The Support Team\n" + "Auth App");

		mailSender.send(message);
	}

	@Override
	public void sendResetPasswordMail(String email, String name, String resetLink) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(email);
		message.setSubject("\"Password Reset Request - Auth App");

		message.setText("Hello " + name + ",\n\n"

				+ "We received a request to reset the password for your account.\n\n"
				+ "To continue with the password reset process, please click the secure link below:\n\n"
				+ resetLink + "\n\n"
				+ "This link will remain valid for 15 minutes.\n\n"
				+ "For additional security, you will also be required to enter the One-Time Password (OTP) sent to your registered email address.\n\n"
				+ "If you did not request a password reset, please ignore this email. Your account remains secure and no changes have been made.\n\n"
				+ "If you believe someone is attempting to access your account without authorization, please contact our support team immediately.\n\n"
				+ "Support Email: support@yourdomain.com\n\n"
				+ "Best regards,\n" + "Security Team\n" + "Auth App");

		mailSender.send(message);
	}

	@Override
	public void sendOtpMail(String email, String name, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(email);
		message.setSubject("Your Password Reset Verification Code");

		message.setText("Hello " + name + ",\n\n"
            + "Please use the following One-Time Password (OTP) to verify your password reset request:\n\n"
            + "OTP: " + otp + "\n\n"
            + "This OTP is valid for 5 minutes and can only be used once.\n\n"
            + "If you did not request a password reset, please ignore this email and consider changing your password if you suspect unauthorized access.\n\n"
            + "Support Email: support@yourdomain.com\n\n"
            + "Best regards,\n"
            + "Security Team\n"
            + "Auth App");

		mailSender.send(message);

	}

	@Override
	public void sendPasswordChangedMail(String email, String name) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(email);
		message.setSubject("Password Changed Successfully");

		message.setText( "Hello " + name + ",\n\n"
            + "This email confirms that the password for your account has been successfully changed.\n\n"
            + "Password Change Time: "
            + LocalDateTime.now()
            + "\n\n"
            + "If you made this change, no further action is required.\n\n"
            + "If you did not change your password, your account may have been compromised. Please contact our support team immediately and secure your account.\n\n"
            + "Support Email: support@yourdomain.com\n\n"
            + "Best regards,\n"
            + "Security Team\n"
            + "Auth App");

		mailSender.send(message);
	}
}

