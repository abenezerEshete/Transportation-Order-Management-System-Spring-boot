package com.abenezer.itom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TransportationOrderManagementSystemApplication {

	public static void main (String[] args) {
		SpringApplication.run (TransportationOrderManagementSystemApplication.class, args);
	}

}
