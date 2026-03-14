package com.ynz.ai.rag.ragdemo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI ragDemoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RAG Demo API")
                        .description("Retrieval-Augmented Generation Demo using Spring AI")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RAG Demo Team")
                                .email("demo@example.com")));
    }
}
