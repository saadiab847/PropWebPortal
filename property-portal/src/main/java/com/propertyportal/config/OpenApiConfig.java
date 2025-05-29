package com.propertyportal.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI propertyPortalOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Property Portal API")
                        .description("RESTful API for Property Portal Application")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Property Portal Team")
                                .email("support@propertyportal.com")
                                .url("https://propertyportal.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("Property Portal Documentation")
                        .url("https://propertyportal.com/docs"))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Default Server URL")
                ));
    }
}