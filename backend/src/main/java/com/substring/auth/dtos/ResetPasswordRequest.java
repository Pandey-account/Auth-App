package com.substring.auth.dtos;

import lombok.Data;

@Data
public class ResetPasswordRequest {

	private String token;
	
	private String otp;
	
	private String password;
}
