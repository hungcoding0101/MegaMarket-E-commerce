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
						config.setAllowedOrigins(List.of());
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
				.ignoringAntMatchers("/user/signup", "/user/reset/passwords/**", "/customer/signup", "/seller/signup",  "/seller/upload/image",
						"/product/suggestions", "/product/getProduct/{id}", "/product/getAllProducts","/product/getProducts",
						"/product/search/**", "/resources/**", "/product/categories/**", "/mrAdmin/**")
				.and()
				.requestMatchers().mvcMatchers("/user/**", "/customer/**", "/seller/**","/product/**", "/resources**","/cart/**", "/order/**",
						"/notification/**", "/csrf")
				.and()
				.authorizeRequests().mvcMatchers("/customer/**", "/order/add", "/order/cancel/**", "/cart/**").hasRole("CUSTOMER")
				.mvcMatchers("/user/signup", "/user/reset/passwords/**", "/customer/signup", "/seller/signup",  "/seller/upload/image",
						"/product/suggestions", "/product/getProduct/{id}", "/product/getAllProducts","/product/getProducts",
						"/product/search/**", "/resources/**", "/product/categories/**", "/mrAdmin/**", "/csrf")
				.permitAll()
				.anyRequest().authenticated();
			}	 	
		}
	

}