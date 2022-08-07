package com.hung.Ecommerce.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.method.annotation.CsrfTokenArgumentResolver;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.hung.Ecommerce.CustomFilters.CustomSecurityFilter;
import com.hung.Ecommerce.Repository.UserRepository;
import com.hung.Ecommerce.Security.CheckBannedTokensFilter2;
import com.hung.Ecommerce.Security.JPAUserDetailsService;
import com.hung.Ecommerce.Util.SecurityAssistant;

@Configuration
public class SecurityConfig{
	
	@Configuration
	@Order(31) // we have to set rank of WebSecurityConfigurerAdapter's filterChains higher then resource server's
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	public static class regularConfig extends WebSecurityConfigurerAdapter{
		private JPAUserDetailsService jPAUserDetailsService;
		
		@Autowired
			public regularConfig(JPAUserDetailsService jPAUserDetailsService) {
				super();
				this.jPAUserDetailsService = jPAUserDetailsService;
			}
	
		@Bean
			public PasswordEncoder passwordEncoder() {
				return new BCryptPasswordEncoder();
			}
	
		@Override
		@Bean
			public AuthenticationManager authenticationManagerBean() throws Exception {
				return super.authenticationManagerBean();
			}
	
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(jPAUserDetailsService);
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.requestMatchers().mvcMatchers("/oauth/authorize")
			.and()
			.cors(c -> {
				CorsConfigurationSource source = request -> {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOrigins(List.of("http://171.226.43.33:3000/","http://192.168.1.4:3000/", "http://192.168.1.3:3000/", "https://megamarket-184661.netlify.app/"));
					config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
					config.setAllowedHeaders(List.of("*"));
					config.setAllowCredentials(true);
					return config; 
				};
				c.configurationSource(source); 
			})
			.csrf().disable()
		    .authorizeRequests().mvcMatchers("/oauth/authorize").authenticated()
		    .and()
		    .formLogin().permitAll();
		}
	}
	
//	@Configuration
//	@Order(19) // we have to set rank of WebSecurityConfigurerAdapter's filterChains higher then resource server's
//		public static class Oauth2FB extends WebSecurityConfigurerAdapter{
//		
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
//	        protected void configure(HttpSecurity http) throws Exception {
////				http.oauth2Login(c -> {
////					c.clientRegistrationRepository(clientRepository());
////				});
//				http.requestMatchers().mvcMatchers("/user/login/facebook").and().authorizeRequests(authorize -> authorize
//	                    .antMatchers("/user/login/facebook").authenticated()
//	                    .anyRequest().permitAll()).oauth2Login(c -> {
//	    					c.clientRegistrationRepository(clientRepository());})
//				.cors(c -> {
//					CorsConfigurationSource source = request -> {
//						CorsConfiguration config = new CorsConfiguration();
//						config.setAllowedOrigins(List.of("http://localhost:3000/", "http://192.168.1.3:3000/", "http://127.0.0.1:3000/",
//								"https://localhost:3000/", "https://192.168.1.3:3000/", "https://127.0.0.1:3000/"));
//						config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//						config.setAllowedHeaders(List.of("*"));
//						config.setAllowCredentials(true);
//						return config; 
//					};
//					c.configurationSource(source); 
//				});
//
//			}
//	}
}

//@Configuration
//@Order(30) // we have to set rank of WebSecurityConfigurerAdapter's filterChains higher then resource server's
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter{
//	
//	private JPAUserDetailsService jPAUserDetailsService;
//	
//	@Autowired
//		public SecurityConfig(JPAUserDetailsService jPAUserDetailsService) {
//			super();
//			this.jPAUserDetailsService = jPAUserDetailsService;
//		}
//
//	@Bean
//		public PasswordEncoder passwordEncoder() {
//			return new BCryptPasswordEncoder();
//		}
//
//	@Override
//	@Bean
//		public AuthenticationManager authenticationManagerBean() throws Exception {
//			return super.authenticationManagerBean();
//		}
//
//	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(jPAUserDetailsService);
//	}
//
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//		.cors(c -> {
//			CorsConfigurationSource source = request -> {
//				CorsConfiguration config = new CorsConfiguration();
//				config.setAllowedOrigins(List.of("http://localhost:3000/", "http://192.168.1.3:3000/", "http://127.0.0.1:3000/"));
//				config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//				config.setAllowedHeaders(List.of("*"));
//				config.setAllowCredentials(true);
//				return config; 
//			};
//			c.configurationSource(source); 
//		})
//		.csrf().disable()
//	    .authorizeRequests().mvcMatchers("/oauth/authorize").authenticated()
//	    .and()
//	    .formLogin().permitAll();
//	}
//	
//
//}
