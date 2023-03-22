package com.aetna.demo;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner{

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		new SpringApplicationBuilder(DemoApplication.class)
				.web(WebApplicationType.SERVLET)
				.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOGGER.error("Logging at ERROR level");
		LOGGER.warn("Logging at WARN level");
		LOGGER.info("Logging at INFO level");
		LOGGER.debug("Logging at DEBUG level");

	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
