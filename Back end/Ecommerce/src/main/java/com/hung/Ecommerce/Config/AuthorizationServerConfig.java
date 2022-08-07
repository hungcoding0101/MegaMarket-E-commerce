package com.hung.Ecommerce.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.hung.Ecommerce.CustomFilters.CustomSecurityFilter;
import com.hung.Ecommerce.Security.CheckBannedTokensFilter2;
import com.hung.Ecommerce.Util.SecurityAssistant;
import com.nimbusds.jwt.JWT;
@Configuration
@Order(10)
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

	@Autowired
		private DataSource dataSource;
	
	@Autowired
		private SecurityAssistant securityAssistant;
	
	
	@Autowired
		private PasswordEncoder passwordEncoder;
	
	@Autowired
		private AuthenticationManager authenticationManager;
	
	@Value("${password}") 
	 private String password; 
	 @Value("${privateKey}") 
	 private String privateKey; 
	 @Value("${alias}") 
	 private String alias;
	
	 

//		 @Bean
//			public CheckBannedTokensFilter2 checkBannedTokensFilter2() {
//			 return new CheckBannedTokensFilter2();
//		 }
		 
	    @Bean
	    @Primary
	    public DefaultTokenServices tokenServices() {
	        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
	        defaultTokenServices.setTokenStore(tokenStore());
	        defaultTokenServices.setSupportRefreshToken(true);
	        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
	        defaultTokenServices.setAccessTokenValiditySeconds(600);
	        defaultTokenServices.setRefreshTokenValiditySeconds(1209600);
	        defaultTokenServices.setReuseRefreshToken(true);
	        return defaultTokenServices;
	    }
	 
	@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager)
			 				.tokenServices(tokenServices()).reuseRefreshTokens(true);
		   
			
		//Add this interceptor to wipe out the session of the browser, so that auth server could be stateless
			endpoints.addInterceptor(new HandlerInterceptor() {

				@Override
				public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
						throws Exception {
					ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
					 
					return HandlerInterceptor.super.preHandle(requestWrapper, response, handler);
				}
				
				@Override
					public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
							ModelAndView modelAndView) throws Exception {
						
					
						if(modelAndView != null && modelAndView.getView() instanceof RedirectView) {
							RedirectView redirectView = (RedirectView) modelAndView.getView();
							String redirectUrl = redirectView.getUrl();
							
							if(redirectUrl.contains("code=") || redirectUrl.contains("error=")) {
								HttpSession session = request.getSession();
						
								if(session != null) {
									session.invalidate();
								}
							}
						}	
				}		
			});
		}
	
	
	
	@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//			JdbcClientDetailsService service = new JdbcClientDetailsService(dataSource);

		clients.inMemory()
		.withClient("my_ecommerce")
		// Since we provided Bcrypt as encoder, the client secret must be encoded by Bcrypt
		.secret(passwordEncoder.encode("myproject0101")).authorizedGrantTypes("authorization_code",
				"refresh_token", "password")
		.scopes("admin").redirectUris("http://localhost:8080/home")
		.and()
		.inMemory()
		.withClient("resourceserver")
		.secret("HelloAuthServer0101");
		}

	
	@Override

		 public void configure(AuthorizationServerSecurityConfigurer security) {
		 	security.tokenKeyAccess("isAuthenticated()");
			
		 	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		 	CorsConfiguration config = new CorsConfiguration();
		 	config.setAllowedOrigins(List.of("https://megamarket-184661.netlify.app/", "http://192.168.1.4:3000/",  "http://192.168.1.3:3000/",
		 			"http://27.65.247.40:3000/"));
			config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
			config.setAllowedHeaders(List.of("*"));
			config.setAllowCredentials(true);
			source.registerCorsConfiguration("/oauth/token", config);
			
			CorsFilter corsFilter = new CorsFilter(source);
		//	security.addTokenEndpointAuthenticationFilter(new CheckBannedTokensFilter2(securityAssistant));
			security.addTokenEndpointAuthenticationFilter(corsFilter);
		 	
		}
	
	//Creates a token store with an access token converter associated to it
	 @Bean
		 public TokenStore tokenStore() {return new JwtTokenStore( 
				 jwtAccessTokenConverter()); 
		 }
	

	 
	//Creates a KeyStoreKeyFactory object to read the private key file from the classpath
	@Bean
		 public JwtAccessTokenConverter jwtAccessTokenConverter() {
			var converter = new JwtAccessTokenConverter();
			 KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory( 
					 															new ClassPathResource(privateKey), password.toCharArray());
			 converter.setKeyPair( keyStoreKeyFactory.getKeyPair(alias));

			 return converter;
	 }

}
