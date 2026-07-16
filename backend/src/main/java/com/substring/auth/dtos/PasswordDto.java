package com.substring.auth.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDto {
	
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;

}
