package com.hung.Ecommerce.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.DTO.RequestToSignUp;
import com.hung.Ecommerce.DTO.RequestToUpdateAddresses;
import com.hung.Ecommerce.Model.Customer;
import com.hung.Ecommerce.Model.Role;
import com.hung.Ecommerce.Model.User;
import com.hung.Ecommerce.Repository.CustomerRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;

@Service
public class CustomerServiceImp implements CustomerService{

	private CustomerRepository customerRepository;
	private UserService userService;
	
	
	@Autowired
		public CustomerServiceImp(CustomerRepository customerRepository, UserService userService) {
			super();
			this.customerRepository = customerRepository;
			this.userService = userService;
		}

	@Transactional(readOnly = true)
	@Override
	public Customer findById(int id, boolean fetchOrNot) {
		return customerRepository.findById(id, fetchOrNot);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Customer> findAll(boolean fetchOrNot) {
		return customerRepository.findAll(fetchOrNot);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Customer> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return customerRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Customer> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return customerRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Transactional
	@Override
	public void save(Customer theOne) {
		 customerRepository.save(theOne);
	}

	@Transactional
	@Override
	public void reconnect(Customer customer) {
		customerRepository.reconnect(customer);
	}

	@Transactional
	@Override
	public Customer update(Customer customer) {
		return customerRepository.update(customer);
	}

	@Transactional
	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return customerRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Transactional
	@Override
	public void delete(int id) {
		customerRepository.delete(id);
	}

	@Transactional
	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return customerRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}
	
	@Transactional
	@Override
		public void signUp(RequestToSignUp request) throws Exception {
			userService.signUp(request);
		}
	
	@Transactional
	@Override
	public void logOut(String accessToken, String refreshToken) {
		userService.logOut(accessToken, refreshToken);
	}

	@Transactional
	@Override
	public void updateAddresses(List<String> addresses, String userName) {
			try {
				List<Customer> matchedUser = customerRepository.findByProperty("username", userName, false, false);
				
				if(!matchedUser.isEmpty()) {
					Customer user = matchedUser.get(0);
					user.setDeliveryAddresses(new HashSet<>(addresses));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
	}
	
	
}
