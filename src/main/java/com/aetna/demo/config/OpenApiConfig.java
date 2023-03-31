package com.aetna.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Contact defaultContact = new Contact();
        defaultContact.setName("Tamzid");
        defaultContact.setUrl("localhost:8080");

        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("User API")
                        .description("User Project Documentations!")
                        .contact(defaultContact)
                        .version("1.0.0"));
    }

}

//swagger url
//http://localhost:8080/swagger-ui/index.html?url=/v3/api-docs&validatorUrl=#/