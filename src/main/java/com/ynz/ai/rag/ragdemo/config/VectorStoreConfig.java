package com.ynz.ai.rag.ragdemo.config;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class VectorStoreConfig {

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        log.info("Configuring SimpleVectorStore for local demo usage");
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    @Bean
    public TokenTextSplitter textSplitter() {
        log.info("Configuring TokenTextSplitter with default chunk size: 800, overlap: 200");
        return new TokenTextSplitter(800, 200, 5, 10000, true, List.of(';', '.', '!', '?', '\n'));
    }
}
