package com.rawan.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/get")
						.filters(f -> f.addRequestHeader("Hello", "World"))
						.uri("http://httpbin.org:80"))
				.route(p -> p
						.path("/posts/**")
						.filters(f -> f.circuitBreaker(config -> config
								.setName("fall")
								.setFallbackUri("forward:/fallback-error")))
						.uri("lb://post-service"))
				.route(p -> p
						.path("/interactions/**")
						.filters(f -> f.circuitBreaker(config -> config
								.setName("fall")
								.setFallbackUri("forward:/fallback-error")))
						.uri("lb://interaction-service"))
				.route(p -> p
						.path("/opinions/**")
						.filters(f -> f.circuitBreaker(config -> config
								.setName("fall")
								.setFallbackUri("forward:/fallback-error")))
						.uri("lb://opinion-service"))
				.route(p -> p
						.path("/auth/**")
						.filters(f -> f.circuitBreaker(config -> config
								.setName("fall")
								.setFallbackUri("forward:/fallback-error")))
						.uri("lb://user-service"))
				.build();
	}

	@RequestMapping("/fallback-error")
	public Mono<String> fallback() {
		return Mono.just("A problem has occured At The API Gateway, please try again later.");
	}

}
