package com.wfh.drawio.ai.config;

import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Title: EmbeddingModelConfig
 * @Author wangfenghuan
 * @Package com.wfh.drawio.ai.config
 * @Date 2026/2/15 16:44
 * @description: 独立的 Embedding 模型配置
 * 使用阿里云 DashScope 的 OpenAI 兼容接口，与聊天模型分离
 */
@Configuration
public class EmbeddingModelConfig {

    @Value("${spring.ai.embedding.dashscope.api-key}")
    private String apiKey;

    @Value("${spring.ai.embedding.dashscope.base-url}")
    private String baseUrl;

    @Value("${spring.ai.embedding.dashscope.model}")
    private String model;

    @Bean
    public EmbeddingModel embeddingModel() {
        OpenAiApi openAiApi = new OpenAiApi.Builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();

        OpenAiEmbeddingOptions options = OpenAiEmbeddingOptions.builder()
                .model(model)
                .build();

        return new OpenAiEmbeddingModel(openAiApi, MetadataMode.EMBED, options);
    }
}
