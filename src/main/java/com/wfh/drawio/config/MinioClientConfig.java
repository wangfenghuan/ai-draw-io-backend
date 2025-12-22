package com.wfh.drawio.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Title: MinioClientConfig
 * @Author wangfenghuan
 * @Package com.wfh.drawio.config
 * @Date 2025/12/22 18:59
 * @description:
 */
@Configuration
@ConfigurationProperties(prefix = "minio.client")
@Data
public class MinioClientConfig {

    private String endpoint;
    private String accessKey;
    private String acessSecret;
    private String bucketName;


    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, acessSecret)
                .build();
    }

}
