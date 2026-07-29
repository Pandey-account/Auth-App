package com.substring.auth.serviceImpl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.substring.auth.services.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@Profile("prod")
	@RequiredArgsConstructor
	public class ResendEmailServiceImpl implements EmailService {

	    private final RestTemplate restTemplate;

	    @Value("${resend.api-key}")
	    private String apiKey;

	    @Value("${resend.from-email}")
	    private String fromEmail;

	    private final ObjectMapper mapper = new ObjectMapper();

	    private void send(String to,
	                      String subject,
	                      String body) {

	        HttpHeaders headers = new HttpHeaders();

	        headers.setBearerAuth(apiKey);

	        headers.setContentType(MediaType.APPLICATION_JSON);

	        Map<String,Object> map = new HashMap<>();

	        map.put("from", fromEmail);
	        map.put("to", List.of(to));
	        map.put("subject", subject);
	        map.put("text", body);

	        HttpEntity<Map<String,Object>> entity =
	                new HttpEntity<>(map, headers);

	        restTemplate.postForEntity(
	                "https://api.resend.com/emails",
	                entity,
	                String.class
	        );
	    }

	    @Override
	    public void sendAccountDeletedMail(String email,
	                                       String name) {

	        send(
	                email,
	                "Account Deleted",
	                "Hello " + name + ", your account has been deleted."
	        );
	    }

	    @Override
	    public void sendResetPasswordMail(
	            String email,
	            String name,
	            String resetLink) {

	        send(
	                email,
	                "Reset Password",
	                "Hello " + name +
	                        "\n\nReset Link:\n" + resetLink
	        );
	    }

	    @Override
	    public void sendOtpMail(
	            String email,
	            String name,
	            String otp) {

	        send(
	                email,
	                "Password Reset OTP",
	                "Hello " + name +
	                        "\nOTP : " + otp
	        );
	    }

	    @Override
	    public void sendPasswordChangedMail(
	            String email,
	            String name) {

	        send(
	                email,
	                "Password Changed",
	                "Hello " + name +
	                        "\nYour password has been changed."
	        );
	    }

	}

