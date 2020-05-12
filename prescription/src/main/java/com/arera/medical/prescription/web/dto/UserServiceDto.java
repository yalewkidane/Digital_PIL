package com.arera.medical.prescription.web.dto;

import com.arera.medical.prescription.model.User;

public class UserServiceDto {

	
	public static User getUser(UserRegistrationDto userRegistrationDto) {
		User user= new User();
		user.setFirstName(userRegistrationDto.getFirstName());
		user.setLastName(userRegistrationDto.getLastName());
		user.setEmail(userRegistrationDto.getEmail());
		user.setPassword(userRegistrationDto.getPassword());
		return user;
	}
}
