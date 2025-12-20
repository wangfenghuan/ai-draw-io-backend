package com.wfh.drawio;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fenghuanwang
 */
@SpringBootApplication
@MapperScan("com.wfh.drawio.mapper")
public class DrawioBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrawioBackendApplication.class, args);
    }

}
