package com.substring.auth.controllers;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.substring.auth.dtos.ForgotPasswordRequest;
import com.substring.auth.dtos.LoginRequest;
import com.substring.auth.dtos.ResetPasswordRequest;
import com.substring.auth.dtos.TokenResponse;
import com.substring.auth.dtos.UserDto;
import com.substring.auth.dtos.refreshTokenRequest;
import com.substring.auth.entities.RefreshToken;
import com.substring.auth.entities.User;
import com.substring.auth.repositories.RefreshTokenRepository;
import com.substring.auth.repositories.UserRepository;
import com.substring.auth.security.CookieService;
import com.substring.auth.security.JwtService;
import com.substring.auth.services.AuthService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class authControll {

	private final UserController userController;

	@Autowired
	private AuthService authService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private CookieService cookieService;
	@Autowired
	private ModelMapper mapper;

	authControll(UserController userController) {
		this.userController = userController;
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		// authenticate
		Authentication authentication = authenticate(loginRequest);
		User user = userRepository.findByEmail(loginRequest.email())
				.orElseThrow(() -> new BadCredentialsException("Invalid Username or Password"));
		if (!user.isEnable()) {
			throw new DisabledException("User is disables");
		}

		String jti = UUID.randomUUID().toString();
		var refershTokenOb = RefreshToken.builder().jti(jti).user(user).createdAt(Instant.now())
				.expiredAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds())).revoked(false).build();

		// refresh token save -- information
		refreshTokenRepository.save(refershTokenOb);

		// access token genrate
		String accessToken = jwtService.generateAccessToken(user);
		String refershToken = jwtService.generateRefreshToken(user, refershTokenOb.getJti());

		// use cookie service to attach refresh token in cookie

		cookieService.attachRefreshCookie(response, refershToken, (int) jwtService.getRefreshTtlSeconds());
		cookieService.addNoStoreHeader(response);

		TokenResponse tokenResponse = TokenResponse.of(accessToken, refershToken, jwtService.getAccessTtlSeconds(),
				mapper.map(user, UserDto.class));
		return ResponseEntity.ok(tokenResponse);
	}

	// access and refresh token renew karne lia api
	@PostMapping("/refresh")
	public ResponseEntity<TokenResponse> refreshToken(@RequestBody(required = false) refreshTokenRequest body,
			HttpServletResponse response, HttpServletRequest request) {

		String refreshToken = readRefreshTokenFromRequest(body, request)
				.orElseThrow(() -> new BadCredentialsException("Refresh token is missing"));

		if (!jwtService.isRefreshToken(refreshToken)) {
			throw new BadCredentialsException("Invalid Refresh Token Type");
		}

		String jti = jwtService.getJti(refreshToken);
		UUID userId = jwtService.getUserId(refreshToken);
		RefreshToken storedRefreshToken = refreshTokenRepository.findByJti(jti)
				.orElseThrow(() -> new BadCredentialsException("Refresh token not recognized"));

		if (storedRefreshToken.isRevoked()) {
			throw new BadCredentialsException("Refresh token expired or revoked");
		}

		if (storedRefreshToken.getExpiredAt().isBefore(Instant.now())) {
			throw new BadCredentialsException("Refresh token expired");
		}

		if (!storedRefreshToken.getUser().getId().equals(userId)) {
			throw new BadCredentialsException("Refresh token does not belong to this user");
		}

		// refresh token ko rotate:

		storedRefreshToken.setRevoked(true);
		String newjti = UUID.randomUUID().toString();
		storedRefreshToken.setReplacedByToken(newjti);
		refreshTokenRepository.save(storedRefreshToken);

		User user = storedRefreshToken.getUser();

		var newRefreshTokenOb = RefreshToken.builder().jti(newjti).user(user).createdAt(Instant.now())
				.expiredAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds())).revoked(false).build();

		refreshTokenRepository.save(newRefreshTokenOb);
		String newAccessToken = jwtService.generateAccessToken(user);
		String newRefreshToken = jwtService.generateRefreshToken(user, newRefreshTokenOb.getJti());

		cookieService.attachRefreshCookie(response, newRefreshToken, (int) jwtService.getRefreshTtlSeconds());
		cookieService.addNoStoreHeader(response);

		return ResponseEntity.ok(TokenResponse.of(newAccessToken, newRefreshToken, jwtService.getAccessTtlSeconds(),
				mapper.map(user, UserDto.class)));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		readRefreshTokenFromRequest(null, request).ifPresent(token -> {
			try {
				if (jwtService.isRefreshToken(token)) {
					String jti = jwtService.getJti(token);
					refreshTokenRepository.findByJti(jti).ifPresent(rt -> {
						rt.setRevoked(true);
						refreshTokenRepository.save(rt);
					});
				}
			} catch (JwtException ignored) {
				// TODO: handle exception
			}

		});
		// use cookieutil(same behaviar)
		cookieService.clearRefreshCookie(response);
		cookieService.addNoStoreHeader(response);
		SecurityContextHolder.clearContext();

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}

	// this method will read refresh tokens from request header or body
	private Optional<String> readRefreshTokenFromRequest(refreshTokenRequest body, HttpServletRequest request) {

		// 1 prefer reading refresh token from cookie

		if (request.getCookies() != null) {

			Optional<String> fromCookie = Arrays.stream(request.getCookies())
					.filter(c -> cookieService.getRefreshTokenCookieName().equals(c.getName())).map(Cookie::getValue)
					.filter(v -> !v.isBlank()).findFirst();

			if (fromCookie.isPresent()) {
				return fromCookie;
			}
		}

		// 2 body
		if (body != null && body.refreshToken() != null && !body.refreshToken().isBlank()) {
			return Optional.of(body.refreshToken());
		}

		// 3 custom header

		String refreshHeader = request.getHeader("X-Refresh-Token");
		if (refreshHeader != null && !refreshHeader.isBlank()) {
			return Optional.of(refreshHeader.trim());
		}

		// 4 Authorization = Bearer <Token>
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader != null && authHeader.regionMatches(0, "Bearer ", 0, 0)) {
			String candidate = authHeader.substring(7).trim();
			if (!candidate.isEmpty()) {
				try {
					if (jwtService.isRefreshToken(candidate)) {
						return Optional.of(candidate);
					}
				} catch (Exception ignored) {
					// TODO: handle exception
				}
			}
		}
		return Optional.empty();
	}

	private Authentication authenticate(LoginRequest loginRequest) {

		try {
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid Username or Password");
		}

	}

	@PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UserDto> registerUser(@ModelAttribute UserDto userDto) {
		System.out.println("===== REGISTER API HIT =====");
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userDto));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> ForgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest,
			HttpServletRequest servletRequest) {

		String ipAddress = servletRequest.getRemoteAddr();

		authService.forgotPassword(forgotPasswordRequest.getIdentifier(), ipAddress);

		return ResponseEntity.ok("Reset link sent successfully");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest passwordRequest,
			HttpServletRequest servletRequest) {

		String ipAddress = servletRequest.getRemoteAddr();

		authService.resetPassword(passwordRequest.getToken(), passwordRequest.getOtp(), passwordRequest.getPassword(), ipAddress);

		return ResponseEntity.ok("Password updated successfully");
	}

	@PostMapping("/resend-otp")
	public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {

		authService.sendOtp(request.get("token"));

		return ResponseEntity.ok("OTP Sent Successfully");
	}

}
