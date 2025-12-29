package com.wfh.drawio.model.dto.diagram;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建图表请求
 *
 * @author fenghuanwang
 */
@Data
@Schema(name = "DiagramAddRequest", description = "图表添加请求")
public class DiagramAddRequest implements Serializable {

    /**
     * 标题
     */
    @Schema(description = "图表标题", example = "系统架构图")
    private String name;

    /**
     * 内容
     */
    @Schema(description = "图表代码", example = "DIAGRAM_20240101_001")
    private String diagramCode;

    /**
     * 图片url
     */
    @Schema(description = "图片URL", example = "https://example.com/image.png")
    private String pictureUrl;


    private static final long serialVersionUID = 1L;
}