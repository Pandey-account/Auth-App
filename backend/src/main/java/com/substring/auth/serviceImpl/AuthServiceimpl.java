package com.substring.auth.serviceImpl;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.substring.auth.dtos.UserDto;
import com.substring.auth.entities.PasswordResetToken;
import com.substring.auth.entities.User;
import com.substring.auth.repositories.PasswordResetTokenRepository;
import com.substring.auth.repositories.UserRepository;
import com.substring.auth.services.AuthService;
import com.substring.auth.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceimpl implements AuthService {

	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailServiceImpl emailService;
	@Autowired
	private SmsServiceImpl smsServiceImpl;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Override
	public UserDto registerUser(UserDto userDto) {

		UserDto userDto1 = userService.createUser(userDto);

		return userDto1;
	}

	public void forgotPassword(String identiFier, String ipAddress) {

		User user = userRepository.findByEmailOrMobileNo(identiFier, identiFier)
				.orElseThrow(() -> new RuntimeException("User Not found"));

		long ipAttempts = passwordResetTokenRepository.countByRequestIpAddressAndCreatedAtAfter(ipAddress,
				LocalDateTime.now().minusHours(1));

		if (ipAttempts >= 5) {
			throw new RuntimeException("Too many requests from this IP address.");
		}

		PasswordResetToken previous = passwordResetTokenRepository.findTopByUserOrderByCreatedAtDesc(user).orElse(null);

		int resetRequests = 1;
		LocalDateTime resetWindowStart = LocalDateTime.now();

		if (previous != null && previous.getResetWindowStart() != null) {

			boolean sameWindow = previous.getResetWindowStart().plusHours(24).isAfter(LocalDateTime.now());

			if (sameWindow) {

				if (previous.getResetRequests() >= 10) {
					throw new RuntimeException("Maximum reset requests exceeded. Try again after 24 hours.");
				}

				resetRequests = previous.getResetRequests() + 1;
				resetWindowStart = previous.getResetWindowStart();
			}
		}

		String token = UUID.randomUUID().toString();

		PasswordResetToken reset = PasswordResetToken.builder().user(user).token(token).otpAttempts(0).resendAttempts(0)
				.resetRequests(resetRequests).resetWindowStart(resetWindowStart).requestIpAddress(ipAddress)
				.tokenExpiresAt(LocalDateTime.now().plusMinutes(15)).createdAt(LocalDateTime.now()).build();

		passwordResetTokenRepository.save(reset);

		String resetLink = "http://localhost:5173/reset-password?token=" + token;

		emailService.sendResetPasswordMail(user.getEmail(), user.getName(), resetLink);

		// Production
		// smsServiceImpl.sendOtpSms(user.getMobileNo(), otp);
	}

	public void resetPassword(String token, String otp, String newPassword, String ipAddress) {

		PasswordResetToken reset = passwordResetTokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Invalid Token"));

		if (reset.getTokenExpiresAt().isBefore(LocalDateTime.now())) {

			throw new RuntimeException("Reset Link Expired");
		}
		if (reset.getOtp() == null) {
			throw new RuntimeException("OTP has not been generated yet.");
		}

		if (reset.getOtpExpiresAt() == null) {
			throw new RuntimeException("OTP has not been generated yet.");
		}
		if (reset.getOtpExpiresAt().isBefore(LocalDateTime.now())) {

			throw new RuntimeException("Otp Expired");
		}

		if (reset.getOtpAttempts() >= 3) {
			throw new RuntimeException("Maximum OTP attempts exceeded");
		}

		if (!reset.getOtp().equals(otp)) {

			reset.setOtpAttempts(reset.getOtpAttempts() + 1);

			passwordResetTokenRepository.save(reset);

			throw new RuntimeException("Invalid OTP");
		}

		User user = reset.getUser();

		user.setPassword(passwordEncoder.encode(newPassword));

		userRepository.save(user);

		emailService.sendPasswordChangedMail(user.getEmail(), user.getName());

		passwordResetTokenRepository.delete(reset);
	}

	@Override
	public void sendOtp(String token) {

		PasswordResetToken reset = passwordResetTokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Invalid Token"));

		if (reset.getTokenExpiresAt().isBefore(LocalDateTime.now())) {

			throw new RuntimeException("Reset link expired");
		}

		if (reset.getResendWindowStart() == null
				|| reset.getResendWindowStart().plusHours(1).isBefore(LocalDateTime.now())) {

			reset.setResendAttempts(0);
			reset.setResendWindowStart(LocalDateTime.now());
		}

		if (reset.getResendAttempts() >= 3) {
			throw new RuntimeException("OTP resend limit exceeded. Try again after 1 hour.");
		}
		String otp = String.format("%06d", new Random().nextInt(1000000));

		reset.setOtp(otp);

		reset.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));

		reset.setOtpAttempts(0);

		reset.setResendAttempts(reset.getResendAttempts() + 1);

		passwordResetTokenRepository.save(reset);

		emailService.sendOtpMail(reset.getUser().getEmail(), reset.getUser().getName(), otp);

		// Production
		// smsServiceImpl.sendOtpSms(reset.getUser().getMobileNo(), otp);

	}

}
