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
					config.setAllowedOrigins(List.of());
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
}


