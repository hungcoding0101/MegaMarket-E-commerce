package com.hung.Ecommerce;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import com.hung.Ecommerce.CustomFilters.CustomSecurityFilter;
import com.hung.Ecommerce.Security.CheckBannedTokensFilter2;
import com.hung.Ecommerce.Service.IndexService;
import com.hung.Ecommerce.Util.SecurityAssistant;

@SpringBootApplication
@EnableAutoConfiguration(exclude={JmxAutoConfiguration.class})
@EnableJpaRepositories(basePackages = {"com.hung.Ecommerce.Repository", "com.hung.Ecommerce.Config"})
public class EcommerceApplication{

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(EcommerceApplication.class, args);
	}
	
	@Bean
		public FilterRegistrationBean<CustomSecurityFilter> checkBannedToken(){
				FilterRegistrationBean<CustomSecurityFilter> filter = new FilterRegistrationBean<>();
				filter.setFilter(new CustomSecurityFilter(securityAssistant()));
				filter.setOrder(-500);
				
				return filter;
	}
	
	@Bean
		public SecurityAssistant securityAssistant() {
				return new SecurityAssistant();
	}
	
}
