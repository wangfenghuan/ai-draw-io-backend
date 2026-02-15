package com.wfh.drawio.config;

/**
 * @Author FengHuan Wang
 * @Date 2025/5/26 15:55
 * @Version 1.0
 */
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "spring.datasource.postgres")
public class PostgresDataSourceProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;

}
