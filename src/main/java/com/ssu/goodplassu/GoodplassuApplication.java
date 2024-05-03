package com.ssu.goodplassu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GoodplassuApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodplassuApplication.class, args);
	}

}
