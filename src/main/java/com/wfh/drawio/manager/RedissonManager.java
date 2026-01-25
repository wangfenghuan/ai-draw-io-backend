package com.wfh.drawio.manager;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Title: RedissonManager
 * @Author wangfenghuan
 * @Package com.wfh.drawio.manager
 * @Date 2025/12/27 16:55
 * @description:
 */
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedissonManager {

    private String host;

    private int port;

    private int database;

    private String password;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%s", host, port))
                .setDatabase(database)
                .setConnectionMinimumIdleSize(1)
                .setConnectionPoolSize(10)
                .setIdleConnectionTimeout(30000)
                .setConnectTimeout(5000)
                .setTimeout(3000)
                .setRetryAttempts(3)
                .setRetryInterval(1500)
                .setPassword(password);
        return Redisson.create(config);
    }

}
