package com.substring.auth.controllers;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.substring.auth.config.AppConstants;
import com.substring.auth.dtos.PasswordDto;
import com.substring.auth.dtos.UpdateDto;
import com.substring.auth.dtos.UserDto;
import com.substring.auth.services.FileStorageService;
import com.substring.auth.services.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/users")
//@AllArgsConstructor
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private FileStorageService fileStorageService;

	// create user api

	@PostMapping(
		    value = "/",
		    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
		)
		public ResponseEntity<UserDto> createUser(
		        @ModelAttribute UserDto userDto) {
		
		    return ResponseEntity.status(HttpStatus.CREATED)
		            .body(userService.createUser(userDto));
		}

	// get all user api
	@GetMapping("/")
	public ResponseEntity<Iterator<UserDto>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUser());
	}

	// get user by email
	@PreAuthorize("hasRole('"+ AppConstants.ADMIN_ROLE +"')")
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {

		return ResponseEntity.ok(userService.getUserByEmail(email));
	}

	// get user by userId
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable("userId") String userId) {

		return ResponseEntity.ok(userService.getUserById(userId));
	}

	// delete User

	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable("userId") String userId) {

		userService.deleteUser(userId);
	}

	// update User
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable("userId") String userId) {

		return ResponseEntity.ok(userService.updateUser(userDto, userId));
	}

	// updated user with email
	@PutMapping("/email/{userEmail}")
	public ResponseEntity<UserDto> updatedUserWithEmail(@RequestBody UserDto userDto,
			@PathVariable("userEmail") String userEmail) {

		return ResponseEntity.ok(userService.updateUserWithEmail(userDto, userEmail));
	}

	// delete User

	@DeleteMapping("/profile")
	public void deleteUserWithEmail(Authentication authentication) {
		
		String email =  authentication.getName();
		userService.deleteUserWithEmail(email);
	}
	
	@GetMapping("/profile-picture")
    public ResponseEntity<Resource> getProfilePicture(Authentication authentication)
            throws MalformedURLException, IOException {

        return fileStorageService.getProfilePicture(authentication);
    }
	
	@PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UserDto>updateUserNameProfilePicture(@ModelAttribute UpdateDto updateDto, Authentication authentication){
		
		String email =  authentication.getName();
		return ResponseEntity.ok(userService.updateUserNamePicture(updateDto, email));
	}
	
	@PutMapping("/change-password")
	public ResponseEntity<UserDto>updatePassword(@RequestBody PasswordDto passwordDto,Authentication authentication){
		
		String  email = authentication.getName();
		return ResponseEntity.ok(userService.changePassword(passwordDto, email));
	}

}
