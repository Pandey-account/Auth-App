package com.substring.auth.exceptions;

import javax.naming.AuthenticationException;
import javax.security.auth.login.CredentialExpiredException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.substring.auth.dtos.ApiError;
import com.substring.auth.dtos.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// resource Not found Exception handler method
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {

		ErrorResponse internalServererror = new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, 404);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(internalServererror);
	}

	// illegalArgument exception method
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {

		ErrorResponse internalServererror = new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, 400);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(internalServererror);
	}

	@ExceptionHandler({ UsernameNotFoundException.class, BadCredentialsException.class,
			CredentialExpiredException.class, DisabledException.class
//	ExpiredJwtException.class,
//	JwtException.class,
//	AuthenticationException.class
	})
	public ResponseEntity<ApiError> handleAuthException(Exception e, HttpServletRequest request) {

		logger.info("Exception : {}", e.getClass().getName());
		var apiError = ApiError.of(HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.badRequest().body(apiError);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiError> handleRuntime(RuntimeException ex, HttpServletRequest request) {

		ApiError error = ApiError.of(HttpStatus.BAD_REQUEST.value(), "Validation Error", ex.getMessage(),
				request.getRequestURI(), true);

		return ResponseEntity.badRequest().body(error);
	}
	
	
}
