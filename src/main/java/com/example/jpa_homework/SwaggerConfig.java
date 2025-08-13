package com.example.jpa_homework;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl("https://api.buanth.site"); // your domain
        server.setDescription("Production Server");
        return new OpenAPI().servers(List.of(server));
    }
}
