package com.ynz.ai.rag.ragdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class VectorStoreConfig {

    @Bean
    public TokenTextSplitter textSplitter() {
        log.info("Configuring TokenTextSplitter with default chunk size: 800, overlap: 200");
        return new TokenTextSplitter(800, 200, 5, 10000, true);
    }
}
