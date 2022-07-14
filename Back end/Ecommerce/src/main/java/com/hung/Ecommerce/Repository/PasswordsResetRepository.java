package com.hung.Ecommerce.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hung.Ecommerce.Model.PasswordReset;
import com.hung.Ecommerce.Model.User;

@Repository
public interface PasswordsResetRepository extends JpaRepository<PasswordReset, String>{
	
	PasswordReset findByUser(User user);
}
