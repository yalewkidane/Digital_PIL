package com.arera.medical.prescription.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.arera.medical.prescription.model.User;



public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User save(User registration);
}
