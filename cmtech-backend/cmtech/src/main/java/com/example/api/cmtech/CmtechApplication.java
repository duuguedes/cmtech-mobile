package com.example.api.cmtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CmtechApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmtechApplication.class, args);
	}

}
