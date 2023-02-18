package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CamundaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaApiApplication.class, args);
	}
	
	@Bean
	public RestTemplate createRestTemplate() {
	    return new RestTemplate();
	}

}
