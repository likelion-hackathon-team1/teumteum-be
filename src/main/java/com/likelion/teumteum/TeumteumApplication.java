package com.likelion.teumteum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TeumteumApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeumteumApplication.class, args);
	}

}
