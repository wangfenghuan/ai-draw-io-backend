package com.wfh.drawio.ai.tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Title: DiagramSchemas
 * @Author wangfenghuan
 * @Date 2025/12/20 20:54
 * @description: 定义 AI 调用的工具参数结构 (Input Schemas)
 */
public class DiagramSchemas {

    // 已移除 CreateDiagramRequest，因为 CreateDiagramTool 现在直接接收 String 类型的 xml 参数

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditOperation {
        @NotBlank(message = "Operation type is required")
        @Pattern(regexp = "^(update|add|delete)$", message = "...")
        @JsonProperty("type")
        private String type;

        @NotBlank(message = "cell_id is required")
        @JsonProperty("cell_id")
        private String cellId;

        @JsonProperty("new_xml")
        private String newXml;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditDiagramRequest {
        @NotEmpty(message = "Operations array is required")
        @JsonProperty("operations")
        private List<EditOperation> operations;
    }
}