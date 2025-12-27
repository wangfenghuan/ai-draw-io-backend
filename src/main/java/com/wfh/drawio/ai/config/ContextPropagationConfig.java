package com.wfh.drawio.ai.config;

import io.micrometer.context.ContextRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;

/**
 * @Title: ContextPropagationConfig
 * @Author wangfenghuan
 * @Package com.wfh.drawio.ai.config
 * @Date 2025/12/26 15:01
 * @description:
 */
@Configuration
public class ContextPropagationConfig {

    @PostConstruct
    public void init() {
        ContextRegistry registry = ContextRegistry.getInstance();

        // 1. 注册 Sink 访问器 (解决前端收不到推送的核心)
        // 确保 SinkAccessor 类已存在且逻辑正确
        registry.registerThreadLocalAccessor(new SinkAccessor());

        // 2. 注册 ConversationId 访问器
        // 确保 ConversationIdAccessor 类已存在且逻辑正确
        registry.registerThreadLocalAccessor(new ConversationIdAccessor());

        // 3. 启用 Reactor 的自动上下文传播机制
        // 这会让 Reactor 操作符（如 map, flatMap）在切换线程时自动搬运上述注册的 ThreadLocal
        Hooks.enableAutomaticContextPropagation();
    }

}
