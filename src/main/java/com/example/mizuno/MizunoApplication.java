package com.example.mizuno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MizunoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MizunoApplication.class, args);
	}

}
