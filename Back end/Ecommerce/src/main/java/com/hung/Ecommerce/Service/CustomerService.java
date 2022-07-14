package com.hung.Ecommerce.Service;

import java.util.List;

import com.hung.Ecommerce.DTO.RequestToSignUp;
import com.hung.Ecommerce.DTO.RequestToUpdateAddresses;
import com.hung.Ecommerce.Model.Customer;
import com.hung.Ecommerce.Model.Role;

public interface CustomerService extends AppService<Customer> {

	public void signUp(RequestToSignUp request) throws Exception;
	public void logOut(String accessToken, String refreshToken);
	public void updateAddresses(List<String> addresses, String userName);
}
