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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

/**
 * @Title: PgVectorStoreConfig
 * @Author wangfenghuan
 * @Package com.wfh.drawio.ai.rag
 * @Date 2026/2/15 15:07
 * @description: PGVector 向量存储配置，支持智能文档加载（内容哈希检测变化）
 */
@Slf4j
@Configuration
public class PgVectorStoreConfig {

    @Resource
    private DocumentLoader loveAppDocumentLoader;

    @Value("${spring.ai.vectorstore.pgvector.dimensions:1024}")
    private int dimensions;

    /** 是否强制重新加载文档（忽略哈希检测） */
    @Value("${spring.ai.rag.force-reload:false}")
    private boolean forceReload;

    private static final int EMBEDDING_BATCH_SIZE = 10;
    private static final String METADATA_TABLE = "vector_store_drawio_metadata";
    private static final String VECTOR_TABLE = "vector_store_drawio";

    @Bean
    public VectorStore pgVectorStore(@Qualifier("postgresJdbcTemplate") JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(dimensions)
                .distanceType(COSINE_DISTANCE)
                .indexType(HNSW)
                .initializeSchema(true)
                .schemaName("public")
                .vectorTableName(VECTOR_TABLE)
                .maxDocumentBatchSize(EMBEDDING_BATCH_SIZE)
                .build();
    }

    @Bean
    public CommandLineRunner loadDocuments(VectorStore pgVectorStore,
                                           @Qualifier("postgresJdbcTemplate") JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                // 1. 加载文档并计算内容哈希
                List<Document> documents = loveAppDocumentLoader.loadDoc();
                if (documents.isEmpty()) {
                    log.info("未发现需要加载的文档，跳过");
                    return;
                }
                String currentHash = computeContentHash(documents);

                // 2. 检查是否需要重新加载
                if (!forceReload && !isReloadNeeded(jdbcTemplate, currentHash)) {
                    log.info("文档内容未变化（hash={}），跳过嵌入加载", currentHash.substring(0, 8));
                    return;
                }

                log.info("检测到文档变化或首次加载，开始嵌入 {} 个文档块...", documents.size());

                // 3. 清空旧数据
                jdbcTemplate.execute("DELETE FROM public." + VECTOR_TABLE);
                log.info("已清空旧向量数据");

                // 4. 分批嵌入并存储
                int totalBatches = (int) Math.ceil((double) documents.size() / EMBEDDING_BATCH_SIZE);
                for (int i = 0; i < documents.size(); i += EMBEDDING_BATCH_SIZE) {
                    int end = Math.min(i + EMBEDDING_BATCH_SIZE, documents.size());
                    List<Document> batch = documents.subList(i, end);
                    pgVectorStore.add(batch);
                    log.info("已加载第 {}/{} 批文档（{} 条）", (i / EMBEDDING_BATCH_SIZE) + 1, totalBatches, batch.size());
                }

                // 5. 保存当前哈希
                saveContentHash(jdbcTemplate, currentHash);
                log.info("全部 {} 个文档块加载完成，哈希已更新: {}", documents.size(), currentHash.substring(0, 8));

            } catch (Exception e) {
                log.warn("文档加载到向量存储失败，应用继续运行: {}", e.getMessage());
            }
        };
    }

    /**
     * 计算所有文档内容的 MD5 哈希值
     */
    private String computeContentHash(List<Document> documents) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            for (Document doc : documents) {
                md.update(doc.getText().getBytes(StandardCharsets.UTF_8));
            }
            return HexFormat.of().formatHex(md.digest());
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * 检查是否需要重新加载（哈希不一致则需要）
     */
    private boolean isReloadNeeded(JdbcTemplate jdbcTemplate, String currentHash) {
        try {
            // 确保元数据表存在
            jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS public." + METADATA_TABLE + " (" +
                "  key VARCHAR(255) PRIMARY KEY, " +
                "  value TEXT, " +
                "  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            String savedHash = jdbcTemplate.queryForObject(
                "SELECT value FROM public." + METADATA_TABLE + " WHERE key = 'content_hash'",
                String.class
            );
            return !currentHash.equals(savedHash);
        } catch (Exception e) {
            // 表为空或查不到记录，说明是首次加载
            return true;
        }
    }

    /**
     * 保存内容哈希到元数据表
     */
    private void saveContentHash(JdbcTemplate jdbcTemplate, String hash) {
        jdbcTemplate.update(
            "INSERT INTO public." + METADATA_TABLE + " (key, value, updated_at) " +
            "VALUES ('content_hash', ?, CURRENT_TIMESTAMP) " +
            "ON CONFLICT (key) DO UPDATE SET value = ?, updated_at = CURRENT_TIMESTAMP",
            hash, hash
        );
    }
}

