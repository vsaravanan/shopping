package com.saravan.mastercard;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

@Log4j2
public class ShoppingServiceApplication {
	public static void main(String[] args) {

		log.info("Before Starting application");

		SpringApplication.run(ShoppingServiceApplication.class, args);
	}

}
