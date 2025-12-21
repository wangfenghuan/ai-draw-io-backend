package com.wfh.drawio.ai.tools;


import jakarta.annotation.Resource;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Title: ToolRegisteration
 * @Author wangfenghuan
 * @Package com.wfh.drawio.ai.tools
 * @Date 2025/12/20 20:41
 * @description: 工具注册器
 *
 * 工具现在由 Spring AI 自动扫描发现，无需手动注册
 */
@Configuration
public class ToolRegisteration {

    @Resource
    private CreateDiagramTool createDiagramTool;

    @Resource
    private AppendDiagramTool appendDiagramTool;

    @Resource
    private EditDiagramTool editDiagramTool;

    @Bean
    public ToolCallback[] allToolCallbacks(){
        return ToolCallbacks.from(
                createDiagramTool,
                appendDiagramTool,
                editDiagramTool
        );
    }

}
