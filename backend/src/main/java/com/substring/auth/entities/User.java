package com.substring.auth.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
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

@Entity
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id")
	private UUID Id;
	@Column(name = "user_email", unique = true, length = 300)
	private String email;
	@Column(name = "name", length = 300)
	private String name;
	private String password;
	@Column(unique = true)
	private String mobileNo;
	private String imageFile;
	private boolean enable = true;
	private Instant createdAt = Instant.now();
	private Instant updateAt = Instant.now();

	@Enumerated(EnumType.STRING)
	private Provider provider = Provider.LOCAL;
	private String providerId;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@PrePersist
	protected void onCreate() {
		Instant now = Instant.now();
		if (createdAt == null)
			createdAt = now;
		updateAt = now;
	}

	@PreUpdate
	protected void onUpdate() {
		updateAt = Instant.now();

	}

	@OneToMany(
		    mappedBy = "user",
		    cascade = CascadeType.ALL,
		    orphanRemoval = true
		)
		private List<RefreshToken> refreshTokens = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<SimpleGrantedAuthority> Authority = 
				roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).toList();
		return Authority;
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enable;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}

}
