package com.substring.auth.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.substring.auth.entities.PasswordResetToken;
import com.substring.auth.entities.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	Optional<PasswordResetToken> findByToken(String token);
	
	void deleteByUser(User user);
	
	Optional<PasswordResetToken>findTopByUserOrderByCreatedAtDesc(User user);

	long countByRequestIpAddressAndCreatedAtAfter(String requestIpAddress, LocalDateTime createdAt);
}
