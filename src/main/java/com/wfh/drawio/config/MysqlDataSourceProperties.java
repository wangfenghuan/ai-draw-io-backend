package com.wfh.drawio.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author FengHuan Wang
 * @Date 2025/5/26 16:09
 * @Version 1.0
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "spring.datasource.mysql")
public class MysqlDataSourceProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
