package com.substring.auth.services;

import java.util.Iterator;

import com.substring.auth.dtos.PasswordDto;
import com.substring.auth.dtos.UpdateDto;
import com.substring.auth.dtos.UserDto;

public interface UserService {
	
	UserDto createUser(UserDto userDto);
	
	UserDto updateUser(UserDto userDto,String userId);
	
	UserDto updateUserWithEmail(UserDto userDto,String userEmail);
	
	UserDto getUserByEmail(String email);
	
	void deleteUser(String userId);
	
	void deleteUserWithEmail(String email);
	
	UserDto getUserById(String userId);
	
	Iterator<UserDto>getAllUser();
	
	UserDto updateUserNamePicture(UpdateDto updateDto,String userEmail);
	
	UserDto changePassword(PasswordDto passwordDto,String UserEmail);
	
}
