package com.substring.auth.dtos;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.substring.auth.entities.Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
	
	private UUID Id;
	private String email;
	private String name;
	private String mobileNo;
	private String password;
	private String confirmPassword;
	private MultipartFile imageFile;
	private boolean enable = true;
	private Instant createdAt = Instant.now();
	private Instant updateAt = Instant.now();
	
	
	private Provider provider = Provider.LOCAL;
	private Set<RoleDto> roles = new HashSet<>();
	
	
}
