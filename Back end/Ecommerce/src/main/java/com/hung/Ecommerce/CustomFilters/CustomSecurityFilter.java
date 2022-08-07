package com.hung.Ecommerce.CustomFilters;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hung.Ecommerce.Util.EnhancedHttpServletRequest;
import com.hung.Ecommerce.Util.SecurityAssistant;
import static java.lang.System.*;

public class CustomSecurityFilter extends OncePerRequestFilter{

		private SecurityAssistant securityAssistant;

	public CustomSecurityFilter(SecurityAssistant securityAssistant) {
	super();
	this.securityAssistant = securityAssistant;
}

	
	// This filter plays a key role in this website's security process
	// I chose to implement authentication and authorization via JWTs put in cookies
	// So this filter will extract tokens from cookies, check if those tokens were valid or added to black list
	// It also intercept responses and modify cookies when needed
	
	@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
		
		final Cookie[] cookies = request.getCookies();
		ContentCachingResponseWrapper  responseWrapper = new ContentCachingResponseWrapper(response);
		EnhancedHttpServletRequest newRequest = new EnhancedHttpServletRequest(request,
				null, null);
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(newRequest);
		
		Cookie accessTokenCookie = new Cookie("access_token", null);
		Cookie refreshTokenCookie = new Cookie("refresh_token", null);
		String accessToken = "";
		String refreshToken = "";
		
		
		if(request.getHeader("contain_token") != null) {
			if(cookies != null) {
				
				 accessTokenCookie = Arrays.stream(cookies)
						.filter(cookie -> cookie.getName().equals("access_token"))
						.findFirst()
						.orElse(null);
				
				 refreshTokenCookie = Arrays.stream(cookies)
						.filter(cookie -> cookie.getName().equals("refresh_token"))
						.findFirst()
						.orElse(null);
				
				Map<String, String> bearerHeader = new HashMap<String, String>();
				Map<String, String[]> additionalParams = new HashMap<String, String[]>();
				
				if(accessTokenCookie != null && !requestWrapper.getRequestURI().contains("/oauth/token")) {
					accessToken = accessTokenCookie.getValue();
						if(securityAssistant.checkWhetherBanned(accessToken) 
							|| securityAssistant.checkWeatherInvalidated(accessToken)) {
							response.reset();
							response.sendError(HttpStatus.UNAUTHORIZED.value(), "This access token has been revoked");
							return;
						}		
						
						out.println("HERE: ACCESS TOKEN: " + accessToken);
						bearerHeader.put(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
				}
				
				if(refreshTokenCookie != null && requestWrapper.getRequestURI().contains("/oauth/token")) {
					refreshToken = refreshTokenCookie.getValue(); 
					if(securityAssistant.checkWhetherBanned(refreshToken)
						|| securityAssistant.checkWeatherInvalidated(refreshToken)) {
						response.reset();
						response.sendError(HttpStatus.UNAUTHORIZED.value(), "This refresh token has been revoked");
						return;
					}
					out.println("HERE: REFRESH TOKEN: " + refreshToken);
					additionalParams.put("refresh_token", new String[] { refreshToken });
				}
				
				 newRequest = new EnhancedHttpServletRequest(request, additionalParams, bearerHeader);	
				 requestWrapper = new ContentCachingRequestWrapper(newRequest);
				 
			}
		}
		
		
		filterChain.doFilter(requestWrapper, responseWrapper);
		
		
		if(requestWrapper.getRequestURI().contains("/oauth/token")) {
			 byte[] responseArray = responseWrapper.getContentAsByteArray();
		     String responseBody = new String(responseArray, responseWrapper.getCharacterEncoding());
		     
		     byte[] requestArray = requestWrapper.getContentAsByteArray();
		     String requestBody = new String(requestArray, requestWrapper.getCharacterEncoding());
		     
		     System.out.println("HERE: RESPONSE: " + responseBody); 
	    	 System.out.println("HERE: REQUEST: " + requestBody);
		     ObjectMapper mapper = new ObjectMapper(); 

		     
		     if(responseBody != null && !responseBody.isBlank()) {

			     Map<String, Object> responseMap = mapper.readValue(responseBody, HashMap.class);
			     
			     String access_token = responseMap.get("access_token").toString();
			     String refresh_token = responseMap.get("refresh_token").toString();
			     
		    	 	int maxAge = 7200;
		    	 	String rememberme = requestWrapper.getParameter("rememberme");
			     
			     if(access_token != null || refresh_token != null) {
				    	 if(access_token != null) {
					    	 	Cookie cookie = new Cookie("access_token", access_token);
					    	 	cookie.setHttpOnly(true);
					    	// 	cookie.setSecure(true);
					    	 	cookie.setMaxAge(595);
					    	 	cookie.setPath("/");
					    	 	response.addCookie(cookie);	    	 	
				     }
				     
				     
				     if(refresh_token != null) {
				    	 	if(!requestBody.contains("grant_type=refresh_token")) {
				    	 		Cookie cookie = new Cookie("refresh_token", refresh_token);
					    	 	cookie.setHttpOnly(true);
					    	 	if(rememberme != null && rememberme.equals("true")) {
					    	 			maxAge = 1209500;
					    	 	}
					    	 	cookie.setMaxAge(maxAge); 
					    	// 	cookie.setSecure(true);
					    	 	cookie.setPath("/"); 
					    	 	response.addCookie(cookie);
				    	 	}
				     }
			     }
			     
			     else {
			    	 responseWrapper.copyBodyToResponse();
			     }
		    }
		    
		}
		
	     else {
	    	 responseWrapper.copyBodyToResponse();
	     }
	     
	     if(requestWrapper.getRequestURL().toString().contains("/user/logout")) {
	    	 	if(accessTokenCookie != null) {
	    	 		accessTokenCookie.setMaxAge(0);
		    	 	accessTokenCookie.setValue("");
//		    	 	accessTokenCookie.setSecure(true);
		    	 	accessTokenCookie.setPath("/");
		    	 	
	    	 	}
				 
	    	 	if(refreshTokenCookie != null) {
	    	 		refreshTokenCookie.setMaxAge(0);
		    	 	refreshTokenCookie.setValue("");
//		    	 	refreshTokenCookie.setSecure(true);
		    	 	refreshTokenCookie.setPath("/");
	    	 	}

	    	 	response.addCookie(accessTokenCookie);
	    	 	response.addCookie(refreshTokenCookie);

	     }
	     
	}

}
