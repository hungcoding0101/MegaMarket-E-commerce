package com.hung.Ecommerce.Controller;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hung.Ecommerce.CustomExceptions.GeneralException;
import com.hung.Ecommerce.CustomExceptions.InvalidUserInfoException;
import com.hung.Ecommerce.DTO.RequestToResetPasswords;
import com.hung.Ecommerce.DTO.RequestToSignUp;
import com.hung.Ecommerce.DTO.RequestToUpdateUserInfo;
import com.hung.Ecommerce.Service.UserService;

import static java.lang.System.*;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
		private UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> SignUp(@RequestBody RequestToSignUp request, HttpServletRequest httpRequest,
									HttpServletResponse httpResponse) throws GeneralException {
	
		try {
			userService.signUp(request);
			return ResponseEntity.status(HttpStatus.CREATED).build();

		} catch (InvalidUserInfoException e) {
			e.printStackTrace();
			String message = "";
			for(String fieldName: e.getViolations().keySet()) {
					message = message.concat(fieldName + " " + e.getViolations().get(fieldName) + ".");
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
		}catch (Exception e) {
			e.printStackTrace();
			throw new GeneralException("Some error occurred");
		}
	}

	@GetMapping("/login")
		public void loggin(OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String role = authentication.getAuthorities().toArray()[0].toString();
			String newURL = request.getRequestURI().replace("user/login", role.replace("ROLE_", "").toLowerCase() + "/getAllInfo");
			request.getRequestDispatcher(newURL).forward(request, response);
	}

	
	@GetMapping("/reset/passwords/require")
		public ResponseEntity<String> sendPassResetCode(@RequestParam String email) throws GeneralException {
					
			try {
				userService.createPasswordsReset(email);
				return ResponseEntity.ok("");
			} catch (ResponseStatusException e) {
				throw e;
			}catch (Exception e) {
				throw new GeneralException("Some errors occurred");
			}		
	}
	
	@PostMapping("/reset/passwords/handle")
		public ResponseEntity<String> handleResetPasswords(@RequestBody RequestToResetPasswords request) throws Exception{
				if(request.getNewPasswords() == null || request.getCode() == null) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new passwords and reset code are required");
				}
				
				try {
					userService.resetPasswords(request.getCode(), request.getNewPasswords());
					return ResponseEntity.ok(null);
				} catch (ResponseStatusException e) {
					throw e;
				} catch (Exception e) {
					throw e;
				}
	}
	
	@PatchMapping("/update/basicInfo")
		public ResponseEntity<List<String>> updateBasicInfo(@RequestBody RequestToUpdateUserInfo request, 
											OAuth2Authentication authentication, HttpServletRequest servletRequest) throws GeneralException{
				try {
						String username = authentication.getName();
						List<String> updatedFields = userService.updateUserInfo(request, username);
						return ResponseEntity.ok(updatedFields);
						
				} catch (InvalidUserInfoException e) {
						e.printStackTrace();
						String message = "";
						for(String fieldName: e.getViolations().keySet()) {
								message = message.concat(fieldName + " " + e.getViolations().get(fieldName) + ".");
						}
						
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
				}catch (ResponseStatusException e) {
						throw e;
				}catch (Exception e) {
						e.printStackTrace();
						throw new GeneralException("Some error occurred");
				}
		}
	
	@GetMapping("/logout")
		public void logOut( HttpServletRequest request, HttpServletResponse response) throws IOException {
			final Cookie[] cookies = request.getCookies();
			
			Cookie refreshCookie = Arrays.stream(cookies)
						.filter(cookie -> cookie.getName().equals("refresh_token"))
						.findFirst()
						.orElse(null);
			
			Cookie accessCookie = Arrays.stream(cookies)
					.filter(cookie -> cookie.getName().equals("access_token"))
					.findFirst()
					.orElse(null);
			
			if(refreshCookie == null || accessCookie == null) {
				response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing token");
				return;
			}
			
			String accessToken = accessCookie.getValue();	
			
			String refreshToken  = refreshCookie.getValue();

			try {
				userService.logOut(accessToken, refreshToken);
			} catch (Exception e) {
				throw e;
			}		
	}
}
