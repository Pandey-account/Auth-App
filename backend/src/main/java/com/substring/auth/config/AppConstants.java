package com.substring.auth.config;

public class AppConstants {

	public static final String[] AUTH_PUBLIC_URL =  {
	
		"/api/v1/auth/**",
		"/v3/api-docs/**",
		"/swagger-ui.html",
		"/swagger-ui/**",
		
	};
	
	public static final String[] USER_GUEST_URL = {
			"/api/v1/users/",
			"/api/v1/users/profile",
	        "/api/v1/users/profile-picture",
	        "/api/v1/users/change-password"
	};
	
	
	public static final String ADMIN_ROLE="ADMIN";
	public static final String GUEST_ROLE="GUEST";
}
