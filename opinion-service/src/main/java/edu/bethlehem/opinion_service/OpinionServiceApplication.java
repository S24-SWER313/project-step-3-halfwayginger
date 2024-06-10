package edu.bethlehem.opinion_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("edu.bethlehem.opinion_service")
@EnableDiscoveryClient
public class OpinionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpinionServiceApplication.class, args);
	}

}
