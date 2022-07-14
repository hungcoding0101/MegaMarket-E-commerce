package com.hung.Ecommerce.Config;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.util.pattern.PathPatternParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.hung.Ecommerce.CustomConverters.JSONToUploadProductRequest;

@Configuration
@ComponentScan(basePackages = {"com.hung.Ecommerce.Controller"})
@EnableAsync
public class MvcConfig implements WebMvcConfigurer{

	@Bean 
		public CommonsMultipartResolver multipartResolver() throws IOException {
			 CommonsMultipartResolver resolver = new CommonsMultipartResolver();
			 resolver.setDefaultEncoding("utf-8");
			resolver.setResolveLazily(true);
			 return resolver;
		}
	
	@Bean
		public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
			return new ShallowEtagHeaderFilter();
		}
	
	@Override // Enable use of PathPattern
	  public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setPatternParser(new PathPatternParser());
	}
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new JSONToUploadProductRequest());
	}
	
	
	//This filter manages versions of static resources
	@Bean
		  public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
		    return new ResourceUrlEncodingFilter();
		  }
}
