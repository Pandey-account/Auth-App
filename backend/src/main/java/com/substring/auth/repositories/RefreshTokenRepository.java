package com.substring.auth.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.substring.auth.entities.RefreshToken;
import com.substring.auth.entities.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

	Optional<RefreshToken> findByJti(String jti);
	void deleteByUser(User user);
}
