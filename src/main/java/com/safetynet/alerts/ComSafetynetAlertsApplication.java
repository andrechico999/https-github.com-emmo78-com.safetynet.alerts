package com.safetynet.alerts;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Spring Boot App
 * @author Olivier MOREL
 *
 */
@SpringBootApplication
public class ComSafetynetAlertsApplication {
	
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ComSafetynetAlertsApplication.class, args);		
	}
}
