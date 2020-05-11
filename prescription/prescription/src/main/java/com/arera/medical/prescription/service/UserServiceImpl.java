package com.arera.medical.prescription.service;




import java.util.Collection;

import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.arera.medical.prescription.database.UserDBService;
import com.arera.medical.prescription.model.Role;
import com.arera.medical.prescription.model.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	
	
	public User findByEmail(String email) {
        return UserDBService.findByEmail(email);
    }
	
	public User save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return UserDBService.save(user);
	}
	
	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = UserDBService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
            user.getPassword(),
            mapRolesToAuthorities(user.getRoles()));
    }

    private Collection < ? extends GrantedAuthority > mapRolesToAuthorities(Collection < Role > roles) {
        return roles.stream()
        		.map(role -> new SimpleGrantedAuthority(role.getName()))
        		.collect(Collectors.toList());
    }
    
    
    
	

}
