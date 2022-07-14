package com.hung.Ecommerce.Service;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import com.hung.Ecommerce.CustomExceptions.InvalidUserInfoException;
import com.hung.Ecommerce.DTO.RequestToSignUp;
import com.hung.Ecommerce.DTO.RequestToUpdateUserInfo;
import com.hung.Ecommerce.Model.Role;
import com.hung.Ecommerce.Model.User;

public interface UserService extends AppService<User> {

	public void signUp(RequestToSignUp request) throws Exception;
	public void logOut(String accessToken, String refreshToken);
	public void createPasswordsReset(String email);
	public void resetPasswords(String code, String newPass)  throws ResponseStatusException, Exception;
	public List<String> updateUserInfo(RequestToUpdateUserInfo request, String username) throws InvalidUserInfoException,
															ResponseStatusException, Exception;
}
