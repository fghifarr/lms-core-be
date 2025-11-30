package com.ghifar.lms.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods
public class LmsCoreBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsCoreBeApplication.class, args);
	}

}
