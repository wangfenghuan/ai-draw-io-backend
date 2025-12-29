package com.wfh.drawio.model.dto.diagram;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Title: CustomChatRequest
 * @Author wangfenghuan
 * @Package com.wfh.drawio.model.dto.diagram
 * @Date 2025/12/27 11:52
 * @description: 自定义聊天请求
 */
@Data
@Schema(name = "CustomChatRequest", description = "自定义聊天请求")
public class CustomChatRequest implements Serializable {

    /**
     * 对话消息
     */
    @Schema(description = "对话消息", example = "你好，请介绍一下系统架构")
    private String message;

    /**
     * 图表id
     */
    @Schema(description = "图表ID", example = "123456789")
    private String diagramId;

    /**
     * 模型名称
     */
    @Schema(description = "模型名称", example = "gpt-3.5-turbo")
    private String modelId;

    /**
     * 接口
     */
    @Schema(description = "API接口地址", example = "https://api.openai.com/v1")
    private String baseUrl;

    @Schema(description = "API密钥", example = "sk-xxxxxxxx")
    private String apiKey;
}
