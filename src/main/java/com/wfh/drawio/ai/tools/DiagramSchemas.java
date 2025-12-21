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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDiagramRequest {
        @NotBlank(message = "XML content is required")
        @JsonProperty("xml")
        private String xml;
    }

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppendDiagramRequest {
        @NotBlank(message = "XML content is required")
        @JsonProperty("xml")
        private String xml;
    }
}