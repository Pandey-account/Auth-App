package com.substring.auth.security;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.substring.auth.entities.Provider;
import com.substring.auth.entities.RefreshToken;
import com.substring.auth.entities.User;
import com.substring.auth.repositories.RefreshTokenRepository;
import com.substring.auth.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private CookieService cookieService;
	@Value("${app.auth.frontend.success-redirect}")
	private String frontendSuccessUrl;
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		
		logger.info("Successful authentication");
		logger.info(authentication.toString());
		
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		
		//identify user:
		String registerationId = "unknown";
		if(authentication instanceof OAuth2AuthenticationToken token) {
			
			registerationId = token.getAuthorizedClientRegistrationId();
			
		}
		
		logger.info("registrationId: "+registerationId);
		logger.info("user: "+oAuth2User.getAttributes().toString());
		
		User user;
		switch(registerationId) {
			case "google" -> {
				String googleId = oAuth2User.getAttributes().getOrDefault("sub", "").toString();
				String email = oAuth2User.getAttributes().getOrDefault("email", "").toString();
				String name = oAuth2User.getAttributes().getOrDefault("name", "").toString();
				String picture = oAuth2User.getAttributes().getOrDefault("picture", "").toString();
				User newUser = User.builder()
						.email(email)
						.name(name)
						.imageFile(picture)
						.enable(true)
						.provider(Provider.GOOGLE)
						.providerId(googleId)
						.build();
				
				 user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(newUser));
			}
			
			case "github" -> {
				
				String name = oAuth2User.getAttributes().getOrDefault("login", "").toString();
				
				String githubId = oAuth2User.getAttributes().getOrDefault("id", "").toString();
				String image = oAuth2User.getAttributes().getOrDefault("avatar_url", "").toString();
				
				String email = (String) oAuth2User.getAttributes().get("email");
				if(email == null) {
					email = name + "@github.com";
				}
				
				User newUser = User.builder()
						.email(email)
						.name(name)
						.imageFile(image)
						.enable(true)
						.provider(Provider.GITHUB)
						.providerId(githubId)
						.build();
				
				user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(newUser));
			}
		
			default -> {
			throw new RuntimeException("Invalid registration Id");
		}
	}
		//username
		//user email
	    // new userCreate
		
		//jwt token -- token ke sath front -- pe fir redirect
		
		//refresh:
		//user --> refresh token revoked
		
		
		
		//refresh token bana ke dunga
		
		String jti = UUID.randomUUID().toString();
	    RefreshToken refreshTokenOb =	RefreshToken.builder()
	    		.jti(jti)
	    		.user(user)
	    		.revoked(false)
	    		.createdAt(Instant.now())
	    		.expiredAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
	    		.build();
	    
	    refreshTokenRepository.save(refreshTokenOb);
	    String accessToken = jwtService.generateAccessToken(user);
	    String refreshToken = jwtService.generateRefreshToken(user, refreshTokenOb.getJti());
	    
	    cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());
	    
	    
		
	//	response.getWriter().write("Login successful");
	    response.sendRedirect(frontendSuccessUrl);
	}

}
