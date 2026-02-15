package com.wfh.drawio.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


/**
 * @Author FengHuan Wang
 * @Date 2025/5/26 15:32
 * @Version 1.0
 * 多数据源配置：MySQL(主) + PostgreSQL(PGVector向量存储)
 * 统一使用 Druid 连接池
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.mysql.druid")
    public DataSource mysqlDataSource(MysqlDataSourceProperties props) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(props.getUrl());
        dataSource.setUsername(props.getUsername());
        dataSource.setPassword(props.getPassword());
        dataSource.setDriverClassName(props.getDriverClassName());
        return dataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.postgres.druid")
    public DataSource postgresDataSource(PostgresDataSourceProperties props) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(props.getUrl());
        dataSource.setUsername(props.getUsername());
        dataSource.setPassword(props.getPassword());
        dataSource.setDriverClassName(props.getDriverClassName());
        return dataSource;
    }

    @Bean
    public JdbcTemplate postgresJdbcTemplate(@Qualifier("postgresDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
