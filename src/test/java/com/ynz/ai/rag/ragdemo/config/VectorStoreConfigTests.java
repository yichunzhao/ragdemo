package com.ynz.ai.rag.ragdemo.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class VectorStoreConfigTests {

    @Test
    void configuresTokenTextSplitter() {
        var textSplitter = new VectorStoreConfig().textSplitter();

        assertThat(textSplitter).isNotNull();
    }
}
