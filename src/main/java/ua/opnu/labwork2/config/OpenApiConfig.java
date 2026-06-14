package ua.opnu.labwork2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Система бронювання авіаквитків")
                        .version("1.0.0")
                        .description("Backend-сервіс для управління системою бронювання авіаквитків. " +
                                "Система забезпечує керування рейсами, пасажирами, бронюваннями, " +
                                "літаками та класами місць. Реалізовано REST API, DTO, валідацію, " +
                                "централізовану обробку помилок, інтеграцію з PostgreSQL та Spring Boot.")
                        .contact(new Contact()
                                .name("Кафедра інформаційних систем, Одеська Політехніка")
                                .email("support@op.edu.ua")
                                .url("https://op.edu.ua"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}