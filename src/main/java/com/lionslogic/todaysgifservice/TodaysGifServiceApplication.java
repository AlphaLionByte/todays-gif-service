package com.lionslogic.todaysgifservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(defaultConfiguration = TodaysGifServiceDefaultFeignConfig.class)
public class TodaysGifServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(TodaysGifServiceApplication.class, args);

	}

}
