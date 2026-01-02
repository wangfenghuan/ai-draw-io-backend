package com.wfh.drawio.model.dto.diagram;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 编辑图表请求
 *
 * @author fenghuanwang
 */
@Data
@Schema(name = "DiagramEditRequest", description = "图表编辑请求")
public class DiagramEditRequest implements Serializable {

    /**
     * id
     */
    @Schema(description = "图表ID", example = "123456789")
    private Long id;

    /**
     * 标题
     */
    @Schema(description = "图表标题", example = "系统架构图")
    private String title;

    /**
     * 图表描述
     */
    @Schema(description = "图表描述", example = "前后端分离架构")
    private String description;

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