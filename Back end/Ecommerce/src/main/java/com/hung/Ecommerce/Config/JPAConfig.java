package com.hung.Ecommerce.Config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("com.hung.Ecommerce.Model")
public class JPAConfig {

}
