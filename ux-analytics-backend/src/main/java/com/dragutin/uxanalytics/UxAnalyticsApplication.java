package com.dragutin.uxanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UxAnalyticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UxAnalyticsApplication.class, args);
	}

}
