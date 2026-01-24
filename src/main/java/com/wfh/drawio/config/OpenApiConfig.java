package com.wfh.drawio.config; // 替换成你的包名

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 配置类
 * 让 Swagger 文档将 Long 类型展示为 String
 * @author fenghuanwang
 */
@Configuration
public class OpenApiConfig {

    // 静态代码块：在 SpringDoc 初始化前执行
    static {
        // 核心代码：将 Long 类型映射为 String 类型
        SpringDocUtils.getConfig().replaceWithClass(Long.class, String.class);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI();
        // 这里可以继续配置 info, servers 等
    }
}