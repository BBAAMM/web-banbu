package com.banbu.kim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KimApplication {

	public static void main(String[] args) {
		SpringApplication.run(KimApplication.class, args);
	}

}
