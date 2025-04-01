package com.ssafy.Dandelion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DandelionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DandelionApplication.class, args);
	}

}
