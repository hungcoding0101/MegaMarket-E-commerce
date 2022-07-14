package com.hung.Ecommerce.Repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hung.Ecommerce.Model.User;

@Repository
public class CustomUserRepository {

	@Autowired
		private UserRepository userRepository;
	
	@PersistenceContext
		private EntityManager entityManager;
	
}
