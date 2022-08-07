package com.hung.Ecommerce.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.hung.Ecommerce.CustomFilters.CustomSecurityFilter;
import com.hung.Ecommerce.CustomFilters.StaticResourceMappingFilter;
import com.hung.Ecommerce.Model.Role;
import com.hung.Ecommerce.Security.CheckBannedTokensFilter2;
import com.hung.Ecommerce.Service.StaticResourceMapperService;
import com.hung.Ecommerce.Util.SecurityAssistant;

@Configuration
public class ResourceServerConfig{
		
	@Configuration
	@Order(20)
	@EnableResourceServer
		public static class regularConfig extends ResourceServerConfigurerAdapter{
				@Autowired
				private SecurityAssistant SecurityAssistant;
			
			@Autowired
				private StaticResourceMapperService staticResourceMapperService;	
			
			@Override
			public void configure(HttpSecurity http) throws Exception {
				http.cors(c -> {
					CorsConfigurationSource source = request -> {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOrigins(List.of("https://localhost:3000/", "http://192.168.1.4:3000/", "http://192.168.1.3:3000/",
								"https://megamarket-184661.netlify.app/", "http://192.168.1.2:3000/", "http://27.65.247.40:3000/"));
						config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS ", "PATCH"));
						config.setAllowedHeaders(List.of("*"));
						config.setAllowCredentials(true);
						return config;
					};
					c.configurationSource(source);
				})
				.csrf()
//				.disable()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.ignoringAntMatchers("/user/signup", "/user/reset/passwords/**", "/customer/signup", "/seller/signup",  "/seller/upload/image"
						, "/resources/**", "/mrAdmin/**","/products/**"
//						, "/products/getProduct/{id}", "/products/getAllProducts","/products/getProducts",
//						"/products/search/**", "/products/categories/**"
						)
				.and()
				.requestMatchers().mvcMatchers("/user/**", "/customer/**", "/seller/**","/products/**", "/resources**","/cart/**", "/order/**",
						"/notification/**", "/csrf")
				.and()
				.authorizeRequests().mvcMatchers("/customer/**", "/order/add", "/order/cancel/**", "/cart/**").hasRole("CUSTOMER")
				.mvcMatchers("/user/signup", "/user/reset/passwords/**", "/customer/signup", "/seller/signup",  "/seller/upload/image"
						, "/resources/**", "/products/categories/**", "/mrAdmin/**",
						"/products/**"
//						,"/products/getProduct/{id}", "/products/getAllProducts","/products/getProducts",
//						"/products/search/**"
						)
				.permitAll()
				.anyRequest().authenticated();
			}	 	
		}
	
//	@Configuration
//	@Order(18)
//		public static class FBConfig extends WebSecurityConfigurerAdapter{
//		private ClientRegistrationRepository clientRepository() {
//			 var c = clientRegistration();
//			 return new InMemoryClientRegistrationRepository(c);
//		}
//		
//		private ClientRegistration clientRegistration() {
//			 return CommonOAuth2Provider.FACEBOOK.getBuilder("facebook")
//			 .clientId("1006798813300787")
//			 .clientSecret("7353c562886bd106ede92439af2757e8")
//			 .build();
//		}
//		
//			@Override
//	        public void configure(HttpSecurity http) throws Exception {
//				
//				http.requestMatchers().mvcMatchers("/user/login/facebook")
//				.and()
//				.authorizeRequests(authorize -> {
//					try {
//						authorize
//						        .mvcMatchers("/user/login/facebook").authenticated().and().oauth2Login(c -> {
//									c.clientRegistrationRepository(clientRepository());
//								});
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//	             );
//			}
//	}
}

//@Configuration
//@Order(20)
//@EnableResourceServer
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
//
//	@Autowired
//		private SecurityAssistant SecurityAssistant;
//	
//	@Autowired
//		private StaticResourceMapperService staticResourceMapperService;	
//	
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//		http.cors(c -> {
//			CorsConfigurationSource source = request -> {
//				CorsConfiguration config = new CorsConfiguration();
//				config.setAllowedOrigins(List.of("http://localhost:3000/", "http://192.168.1.2:3000/", "http://192.168.1.3:3000/",
//						"http://116.106.3.186:3000/"));
//				config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS ", "PATCH"));
//				config.setAllowedHeaders(List.of("*"));
//				config.setAllowCredentials(true);
//				return config;
//			};
//			c.configurationSource(source);
//		})
//		.csrf().disable()
//		.requestMatchers().mvcMatchers("/user/**","/customer/**", "/seller/**","/product/**", "/resources**","/cart/**", "/order/**").and()
//		.authorizeRequests().mvcMatchers("/order/add", "/order/cancel/**", "/cart/**").hasRole("CUSTOMER")
//		.mvcMatchers("/customer/signup", "/seller/signup",  "/seller/upload/image", "/product/suggestions",
//				"/product/getProduct/{id}", "/product/getProducts", "/product/search/**", "/resources/**", "/product/categories/**",
//				"/mrAdmin/**")
//		.permitAll()
//		.anyRequest().authenticated();
//	}	 	
//}
