package com.hung.Ecommerce.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hung.Ecommerce.Model.User;


public interface UserRepository extends CustomRepository<User>{
	
}
