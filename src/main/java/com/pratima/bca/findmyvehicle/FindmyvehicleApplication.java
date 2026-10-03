package com.pratima.bca.findmyvehicle;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@SpringBootApplication
public class FindmyvehicleApplication {


    @Bean
    public OpenAPI inventoryOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Find My Vehicl API Management")
                        .description("API documentation for Find My Vehicle System")
                        .version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

	public static void main(String[] args) {
		SpringApplication.run(FindmyvehicleApplication.class, args);
	}

}
