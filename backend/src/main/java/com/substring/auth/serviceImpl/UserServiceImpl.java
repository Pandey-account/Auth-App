package com.substring.auth.serviceImpl;

import java.time.Instant;
import java.util.Iterator;
import java.util.UUID;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.substring.auth.config.AppConstants;
import com.substring.auth.dtos.PasswordDto;
import com.substring.auth.dtos.UpdateDto;
import com.substring.auth.dtos.UserDto;
import com.substring.auth.entities.Provider;
import com.substring.auth.entities.Role;
import com.substring.auth.entities.User;
import com.substring.auth.exceptions.ResourceNotFoundException;
import com.substring.auth.helpers.UserHelper;
import com.substring.auth.repositories.RefreshTokenRepository;
import com.substring.auth.repositories.RoleRepository;
import com.substring.auth.repositories.UserRepository;
import com.substring.auth.services.FileStorageService;
import com.substring.auth.services.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private final FileStorageService fileStorageService;;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private EmailServiceImpl emailService;

	@Override
	@Transactional
	public UserDto createUser(UserDto userDto) {
		if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
			throw new IllegalArgumentException("Email is required");
		}

		if (userRepository.existsByEmail(userDto.getEmail())) {
			throw new IllegalArgumentException("User with given email already exists");

		}
		
		
		if (!userDto.getPassword().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")) {

			throw new RuntimeException("Password must contain uppercase, lowercase, number and special character");
		}

		if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {

			throw new RuntimeException("password and Confirm password do not match");
		}

		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

//		if you have extra checks put here...
		User user = modelMapper.map(userDto, User.class);
		user.setProvider(userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL);
		// role assign here to use for authorization

		// assign the default role

		Role role = roleRepository.findByName("ROLE_" + AppConstants.GUEST_ROLE).orElse(null);
		user.getRoles().add(role);

		MultipartFile imageFile = userDto.getImageFile();

		if (imageFile != null && !imageFile.isEmpty()) {

			user.setImageFile(fileStorageService.saveFile(imageFile));

		}

		User savedUser = userRepository.save(user);
		return modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		UUID uId = UserHelper.parseUUID(userId);
		User exitinguser = userRepository.findById(uId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));

		// we are not going to change email because email is unique
		if (userDto.getName() != null)
			exitinguser.setName(userDto.getName());
		if (userDto.getImageFile() != null && !userDto.getImageFile().isEmpty())
			
			fileStorageService.deleteFile(exitinguser.getImageFile());
			exitinguser.setImageFile(fileStorageService.saveFile(userDto.getImageFile()));
			
		if (userDto.getProvider() != null)
			exitinguser.setProvider(userDto.getProvider());
		// change password updation logic
		if (userDto.getPassword() != null)
			exitinguser.setPassword(userDto.getPassword());
		exitinguser.setEnable(userDto.isEnable());
		exitinguser.setUpdateAt(Instant.now());

		User updatedUser = userRepository.save(exitinguser);

		return modelMapper.map(updatedUser, UserDto.class);
	}

	@Override
	public UserDto getUserByEmail(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given email Id  "));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public void deleteUser(String userId) {

		UUID uId = UserHelper.parseUUID(userId);
		User user = userRepository.findById(uId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id "));
		userRepository.delete(user);
	}

	@Override
	public UserDto getUserById(String userId) {

		UUID uid = UserHelper.parseUUID(userId);
		User user = userRepository.findById(uid)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id "));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	@Transactional
	public Iterator<UserDto> getAllUser() {

		return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDto.class)).toList() // Creates
																											// the List
				.iterator();
	}

	@Override
	public UserDto updateUserWithEmail(UserDto userDto, String userEmail) {

		User exitinguser = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given email"));

		// we are not going to change email because email is unique
		if (userDto.getName() != null)
			exitinguser.setName(userDto.getName());
		if (userDto.getImageFile() != null && !userDto.getImageFile().isEmpty())
			
			fileStorageService.deleteFile(exitinguser.getImageFile());
			exitinguser.setImageFile(fileStorageService.saveFile(userDto.getImageFile()));
		    
		if (userDto.getProvider() != null)
			exitinguser.setProvider(userDto.getProvider());
		// change password updation logic
		if (userDto.getPassword() != null)
			exitinguser.setPassword(userDto.getPassword());
		exitinguser.setEnable(userDto.isEnable());
		exitinguser.setUpdateAt(Instant.now());

		User updatedUser = userRepository.save(exitinguser);

		return modelMapper.map(updatedUser, UserDto.class);
	}

	@Override
	@Transactional
	public void deleteUserWithEmail(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given email "));
		
		refreshTokenRepository.deleteByUser(user);
		
		try {
	        emailService.sendAccountDeletedMail(
	            user.getEmail(),
	            user.getName()
	        );
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		fileStorageService.deleteFile(user.getImageFile());
		userRepository.delete(user);

	}

	@Override
	public UserDto updateUserNamePicture(UpdateDto updateDto, String email) {

		User exitingUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given email"));

		if (updateDto.getName() != null && !updateDto.getName().isBlank()) {
			exitingUser.setName(updateDto.getName());
		}

		if (updateDto.getImageFile() != null && !updateDto.getImageFile().isEmpty()) {
			fileStorageService.deleteFile(exitingUser.getImageFile());
			String fileName = fileStorageService.saveFile(updateDto.getImageFile());
			exitingUser.setImageFile(fileName);
		}
		exitingUser.setUpdateAt(Instant.now());
		User updatedUser = userRepository.save(exitingUser);
		return modelMapper.map(updatedUser, UserDto.class);

	}

	@Override
	public UserDto changePassword(PasswordDto passwordDto, String UserEmail) {

		User exitingUser = userRepository.findByEmail(UserEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given email"));

		if (!passwordEncoder.matches(passwordDto.getOldPassword(), exitingUser.getPassword())) {

			throw new RuntimeException("Current password is incorrect");
		}

		if (passwordEncoder.matches(passwordDto.getNewPassword(), exitingUser.getPassword())) {

			throw new RuntimeException("New password must be different from current password");
		}


		if (!passwordDto.getNewPassword().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")) {

			throw new RuntimeException("Password must contain uppercase, lowercase, number and special character");
		}

		if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {

			throw new RuntimeException("New password and Confirm password do not match");
		}

		exitingUser.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));

		exitingUser.setUpdateAt(Instant.now());
		User updatedUser = userRepository.save(exitingUser);
		emailService.sendPasswordChangedMail(exitingUser.getEmail(),exitingUser.getName() );

		return modelMapper.map(updatedUser, UserDto.class);
	}

}
