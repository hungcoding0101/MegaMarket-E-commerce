package com.hung.Ecommerce.Config;

import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CookieConfig {

	@Bean
	  public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
	  return CookieSameSiteSupplier.ofLax();
	}
}
