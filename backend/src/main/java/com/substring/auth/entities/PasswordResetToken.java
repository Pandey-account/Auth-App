package com.substring.auth.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    // OTP link open hone ke baad generate hoga
    @Column(nullable = true)
    private String otp;

    @Column(nullable = false)
    private Integer otpAttempts = 0;

    @Column(nullable = false)
    private Integer resendAttempts = 0;

    @Column(nullable = false)
    private Integer resetRequests = 0;

    // Reset link expiry
    @Column(nullable = false)
    private LocalDateTime tokenExpiresAt;

    // OTP generate hone ke baad set hoga
    @Column(nullable = true)
    private LocalDateTime otpExpiresAt;

    private LocalDateTime resendWindowStart;

    private LocalDateTime resetWindowStart;

    private String requestIpAddress;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean used = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
