package com.substring.auth.dtos;

public record TokenResponse(
		String accessToken,
		String refreshToken,
		long expireIn,
		String tokenType,
		UserDto user
	) {

	public static TokenResponse of(String accessToken,String refreshToken,long expiresIn,UserDto user) {
		return new TokenResponse(accessToken, refreshToken, expiresIn, "Bearer", user);
	}
}
