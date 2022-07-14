package com.hung.Ecommerce.Util;

import static java.lang.System.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hung.Ecommerce.Model.User;
import com.hung.Ecommerce.Service.UserService;

@Service
public class SecurityAssistant {

	private Map<String, LocalDateTime> tokenBackList;
	
	@Autowired
		private UserService userService;

	public SecurityAssistant() {
		super();
		this.tokenBackList = new ConcurrentHashMap<String, LocalDateTime>();
	}
	
	public void addToBackList(String token, int forHowManySecond) {
		Map<String, Object> tokenValues = decodingToken(token);
		String ati = (String) tokenValues.get("ati");
		String jti = ati != null ? ati : (String) tokenValues.get("jti");
		LocalDateTime tokenExpireTime = LocalDateTime.now().plusSeconds(forHowManySecond);
		tokenBackList.put(jti, tokenExpireTime);
		out.println("HERE: PUT TOKEN TO BACK LIST");
	}
	
	@Transactional(readOnly = true)
	public boolean checkWeatherInvalidated(String token) {
			Map<String, Object> tokenValues = decodingToken(token);
			
			if(!tokenValues.containsKey("user_name") || !tokenValues.containsKey("exp")) {
				return true;
			}
			
			String username = (String) tokenValues.get("user_name");
			
			List<User> matchedUser = userService.findByProperty("username", username, false, false);
			if(matchedUser.isEmpty()) {
				return false;
			}
			
			User user = matchedUser.get(0);
			out.println("HERE: TOKEN VALUES: " + tokenValues);
			boolean isRefreshToken = tokenValues.containsKey("ati");
			Integer tokenExp = (Integer) tokenValues.get("exp");
			Integer tokenCreatedAt = null;
			try {
				tokenCreatedAt = tokenExp - (isRefreshToken? 1209600 : 600);
			} catch (NumberFormatException e) {
				return true;
			}
			
			Long PasswordsLastUpdate = user.getPasswordsLastChange();
			
			if(PasswordsLastUpdate != null && tokenCreatedAt < PasswordsLastUpdate + 2) {
				return true;
			}
			
			return false;
	}
	
	
	public boolean checkWhetherBanned(String token) throws UnsupportedEncodingException {
		Map<String, Object> tokenValues = decodingToken(token);
		String ati = (String) tokenValues.get("ati");
		String jti = ati != null ? ati : (String) tokenValues.get("jti");
		out.println("HERE: JTI : " + jti);
		return tokenBackList.containsKey(jti); 
	}
	
	
	public Map<String, Object> decodingToken(String token){
		  String[] tokenParts = token.split("\\.");
	      String b64payload = tokenParts[1];
	      String jsonString;
	      Map<String, Object> valueMap = null;
	      
			try {
				jsonString = new String(Base64.decodeBase64(b64payload), "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
			     valueMap = mapper.readValue(jsonString, HashMap.class);
			    
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return valueMap;
	      
	}
	
	@Scheduled(cron = "0/20 * * * * ?")
		public void cleanningBackList() {
			Iterator<Entry<String, LocalDateTime>> iterator =  this.tokenBackList.entrySet().iterator();
			out.println("\nHERE: CLEANING BACK LIST ");
			while(iterator.hasNext()) {
				Entry<String, LocalDateTime> bannedToken = iterator.next();
				if(bannedToken.getValue().isBefore(LocalDateTime.now())) {
					out.println("\nHERE: REMOVED KEY " + bannedToken.getKey());
					iterator.remove();
				}
			}
		}
}
