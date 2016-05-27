package com.mcnc.socket.config;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class MvcConfig extends WebMvcAutoConfigurationAdapter {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);
		
		registry.addResourceHandler("/**")
			.addResourceLocations("classpath:/static/chat/");
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		 registry.addMapping("/**").allowedOrigins("*");
	}
	
}