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
		package com.substring.auth.security;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.substring.auth.entities.Role;
import com.substring.auth.entities.User;
import com.substring.auth.helpers.UserHelper;
import com.substring.auth.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;

@Component //or @service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserRepository userRepository;
	
	private  Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String header = request.getHeader("Authorization");
		logger.info("Authorization header : {}",header);
		if(header != null && header.startsWith("Bearer ")) {
			
			// token extract and validate then authentication create and then security context ke 
			
			String token = header.substring(7);
			//check for access token
			
			try {
				
				
				if(!jwtService.isAccessToken(token)) {
					//message pass karna hai
					
					filterChain.doFilter(request, response);
					return;
				}
				
				Jws<Claims> parse = jwtService.parse(token);
				Claims payload = parse.getPayload();
				String userId = payload.getSubject();
				UUID userUuid = UserHelper.parseUUID(userId);
				
				userRepository.findById(userUuid)
						.ifPresent( user ->{
							
						//check for user enable	or not
							if(user.isEnable()) {
								//user mil chuka hai database se
								List<GrantedAuthority> authorities = user.getRoles() == null ? List.of(): user.getRoles().stream()
										.map( role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
								
								UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
										user.getEmail(),
										null,
										authorities
							);
								
								authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
								
								// final line : to set authentication to security context
								if(SecurityContextHolder.getContext().getAuthentication() == null)
									SecurityContextHolder.getContext().setAuthentication(authentication);
								
							}
							
							
							});
				
				
			} catch (ExpiredJwtException e) {
				request.setAttribute("error", "Token Expired");
				//e.printStackTrace();
			}catch (MalformedJwtException e) {
				request.setAttribute("error", "Invalid Token");
				//e.printStackTrace();
			} catch (JwtException e) {
				request.setAttribute("error", "Invalid Token");
				//e.printStackTrace();
			} catch (Exception e) {
				request.setAttribute("error", "Invalid Token");
				//e.printStackTrace();
			}
		}
		
	filterChain.doFilter(request, response);
	}
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		
		return request.getRequestURI().startsWith("/api/v1/auth/");
	} 
}

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
