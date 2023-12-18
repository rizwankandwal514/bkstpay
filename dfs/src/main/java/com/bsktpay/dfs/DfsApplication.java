package com.bsktpay.dfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DfsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DfsApplication.class, args);
	}

}
