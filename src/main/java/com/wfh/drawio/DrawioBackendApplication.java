package com.wfh.drawio;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author fenghuanwang
 */
@SpringBootApplication
@MapperScan("com.wfh.drawio.mapper")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 2592000) // 30天 (30 * 24 * 60 * 60)
@EnableScheduling
public class DrawioBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrawioBackendApplication.class, args);
    }

}
