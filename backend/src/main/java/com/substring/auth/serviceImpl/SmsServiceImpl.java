package com.substring.auth.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.substring.auth.services.SmsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

	private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

	@Value("${app.auth.msg91.auth.key}")
	private String authKey;

	@Value("${app.auth.msg91.template.id}")
	private String templateId;

	private final RestTemplate restTemplate;

	@Override
	public void sendOtpSms(String mobileNo, String otp) {

		// Mobile validation
		if (mobileNo == null || !mobileNo.matches("^[6-9]\\d{9}$")) {

			throw new RuntimeException("Invalid mobile number");
		}

		try {

			String url = "https://control.msg91.com/api/v5/otp" + "?template_id=" + templateId + "&mobile=91" + mobileNo
					+ "&otp=" + otp;

			HttpHeaders headers = new HttpHeaders();

			headers.set("authkey", authKey);

			HttpEntity<String> request = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

			logger.info("MSG91 Response : {}", response.getBody());

			if (!response.getStatusCode().is2xxSuccessful()) {

				throw new RuntimeException("Failed to send OTP SMS");
			}

		} catch (Exception ex) {

			logger.error("SMS sending failed : {}", ex.getMessage());

			throw new RuntimeException("Unable to send OTP SMS", ex);
		}
	}
}