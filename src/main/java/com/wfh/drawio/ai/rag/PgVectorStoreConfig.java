package com.wfh.drawio.ai.rag;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

/**
 * @Title: PgVectorStoreConfig
 * @Author wangfenghuan
 * @Package com.wfh.drawio.ai.rag
 * @Date 2026/2/15 15:07
 * @description: PGVector 向量存储配置
 */
@Slf4j
@Configuration
public class PgVectorStoreConfig {

    @Resource
    private DocumentLoader loveAppDocumentLoader;

    @Value("${spring.ai.vectorstore.pgvector.dimensions:1024}")
    private int dimensions;

    @Bean
    public VectorStore pgVectorStore(@Qualifier("postgresJdbcTemplate") JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(dimensions)
                .distanceType(COSINE_DISTANCE)
                .indexType(HNSW)
                .initializeSchema(true)
                .schemaName("public")
                .vectorTableName("vector_store_drawio")
                .maxDocumentBatchSize(10)
                .build();
    }

    /**
     * 应用启动后加载文档到向量库
     * 分批添加，每批最多 BATCH_SIZE 条（DashScope embedding 限制）
     */
    private static final int EMBEDDING_BATCH_SIZE = 10;

    @Bean
    public CommandLineRunner loadDocuments(VectorStore pgVectorStore) {
        return args -> {
            try {
                List<Document> documents = loveAppDocumentLoader.loadDoc();
                if (!documents.isEmpty()) {
                    // 分批添加，避免 DashScope embedding batch size 限制
                    for (int i = 0; i < documents.size(); i += EMBEDDING_BATCH_SIZE) {
                        int end = Math.min(i + EMBEDDING_BATCH_SIZE, documents.size());
                        List<Document> batch = documents.subList(i, end);
                        pgVectorStore.add(batch);
                        log.info("已加载第 {}/{} 批文档（{} 条）", (i / EMBEDDING_BATCH_SIZE) + 1,
                                (int) Math.ceil((double) documents.size() / EMBEDDING_BATCH_SIZE), batch.size());
                    }
                    log.info("全部 {} 个文档块加载完成", documents.size());
                }
            } catch (Exception e) {
                log.warn("文档加载到向量存储失败，应用继续运行: {}", e.getMessage());
            }
        };
    }
}
