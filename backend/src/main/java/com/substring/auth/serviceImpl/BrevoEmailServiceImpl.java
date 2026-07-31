package com.substring.auth.serviceImpl;

import java.time.LocalDateTime;
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

import com.substring.auth.services.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class BrevoEmailServiceImpl implements EmailService {

    private final RestTemplate restTemplate;

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.from-email}")
    private String fromEmail;

    @Value("${brevo.from-name}")
    private String fromName;

    private void sendEmail(String to,
                           String subject,
                           String body) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set("api-key", apiKey);

        Map<String, Object> request = new HashMap<>();

        request.put("sender",
                Map.of(
                        "name", fromName,
                        "email", fromEmail
                ));

        request.put("to",
                List.of(
                        Map.of("email", to)
                ));

        request.put("subject", subject);

        request.put("textContent", body);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(request, headers);

        restTemplate.postForEntity(
                "https://api.brevo.com/v3/smtp/email",
                entity,
                String.class
        );
    }

    @Override
    public void sendAccountDeletedMail(String email, String name) {

        sendEmail(
                email,
                "Account Deleted Successfully",
                "Hello " + name
                        + "\n\nYour account has been deleted."
                        + "\n\nTime : "
                        + LocalDateTime.now()
        );
    }

    @Override
    public void sendResetPasswordMail(String email,
                                      String name,
                                      String resetLink) {

        sendEmail(
                email,
                "Password Reset Request",
                "Hello " + name
                        + "\n\nReset Password using this link:\n\n"
                        + resetLink
                        + "\n\nThis link is valid for 15 minutes."
        );

    }

    @Override
    public void sendOtpMail(String email,
                            String name,
                            String otp) {

        sendEmail(
                email,
                "Password Reset OTP",
                "Hello " + name
                        + "\n\nOTP : "
                        + otp
                        + "\n\nOTP is valid for 5 minutes."
        );

    }

    @Override
    public void sendPasswordChangedMail(String email,
                                        String name) {

        sendEmail(
                email,
                "Password Changed",
                "Hello " + name
                        + "\n\nYour password has been changed successfully."
                        + "\n\nTime : "
                        + LocalDateTime.now()
        );

    }

}