package com.hung.Ecommerce.Security;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hung.Ecommerce.Model.User;
import com.hung.Ecommerce.Repository.UserRepository;

@Service
public class JPAUserDetailsService implements UserDetailsService{

	@Autowired
		private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<User> result;
		
		if(username.contains("@")) {
			result = userRepository.findByProperty("email", username, true, false);
		}
		
		else {result = userRepository.findByProperty("username", username, true, false);}
		
		if(result.isEmpty()) {
			throw new UsernameNotFoundException("No user found for name or email: " + username);}
		
		return new JPAUserDetails(result.get(0));
	}

}
