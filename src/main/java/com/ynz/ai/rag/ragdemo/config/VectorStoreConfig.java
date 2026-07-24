package com.ynz.ai.rag.ragdemo.config;

import java.util.List;
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
        return TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(200)
                .withMinChunkLengthToEmbed(5)
                .withMaxNumChunks(10000)
                .withKeepSeparator(true)
                .withPunctuationMarks(List.of(';', '.', '!', '?', '\n'))
                .build();
    }
}
