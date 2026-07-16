package com.substring.auth.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.substring.auth.dtos.ApiError;
import com.substring.auth.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.csrf(AbstractHttpConfigurer::disable)
	    .cors(Customizer.withDefaults())
	    // Added closing parenthesis here )
	    .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
	    .authorizeHttpRequests(auth -> auth
	        .requestMatchers(AppConstants.AUTH_PUBLIC_URL).permitAll()
	        .requestMatchers(AppConstants.USER_GUEST_URL).hasRole(AppConstants.GUEST_ROLE)
	        .requestMatchers("/api/v1/users/**").hasRole(AppConstants.ADMIN_ROLE)
	        .anyRequest().authenticated()
	    )
	    //oauth2 configuration
	    
	    .oauth2Login(oauth2 ->
	    	oauth2.successHandler(authenticationSuccessHandler)
	    	.failureHandler(null)
	    )
	    
	    .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
	    	authException.printStackTrace();
	        response.setStatus(401);
	        response.setContentType("application/json");
	        String message = "Unauthorized access: " + authException.getMessage();
	        
	        String error = (String) request.getAttribute("error");
	        if(error != null) {
	        	message = error;
	        }
	        
//	        Map<String, String> errorMap = Map.of(
//	            "message", message,
//	            "statusCode", "401"
//	        );

	        var apiError = ApiError.of(HttpStatus.UNAUTHORIZED.value(), "Unauthorized Access", message, request.getRequestURI(),true); 
	        try {
	            String jsonResponse = new ObjectMapper().writeValueAsString(apiError);
	            response.getWriter().write(jsonResponse);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }).accessDeniedHandler((request,response,e) ->{
	    	response.setStatus(403);
	    	response.setContentType("application/json");
	    	String message = e.getMessage();
	    	String error = (String) request.getAttribute("error");
	    	if(error != null) {
	    		message = error;
	    	}
	    	var apiError = ApiError.of(HttpStatus.FORBIDDEN.value(), "Forbidden Access", message, request.getRequestURI(), true);
	    	var objectMapper = new ObjectMapper();
	    	response.getWriter().write(objectMapper.writeValueAsString(apiError));
	    }))
	    
	    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	return http.build();
	} 
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	} 
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		
		return configuration.getAuthenticationManager();
	}
//	@Bean
//	public UserDetailsService users() {
//		
//		User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
//		
//		UserDetails user1 = userBuilder.username("rohit").password("abc").roles("ADMIN").build();
//		
//		return new InMemoryUserDetailsManager(user1);
//	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource(
	        @Value("${app.cors.front-end-url}") String corsUrls) {

	    CorsConfiguration config = new CorsConfiguration();

	    if (corsUrls != null && !corsUrls.isBlank()) {
	        config.setAllowedOrigins(Arrays.asList(corsUrls.split(",")));
	    }

	    config.setAllowedMethods(
	            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
	    );

	    config.setAllowedHeaders(List.of("*"));
	    config.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source =
	            new UrlBasedCorsConfigurationSource();

	    source.registerCorsConfiguration("/**", config);

	    return source;
	}
}
