package com.ynz.ai.rag.ragdemo;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(args = "help", properties = {
		"springdoc.swagger-ui.path=/swagger-ui.html",
		"spring.shell.interactive.enabled=false"
})
class RagDemoApplicationTests {

	@MockitoBean
	private ChatModel chatModel;

	@MockitoBean
	private VectorStore vectorStore;

	@Test
	void contextLoads() {
	}

}
