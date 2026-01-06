package com.wfh.drawio.model.dto.diagram;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新图表请求
 *
 * @author fenghuanwang
 */
@Data
@Schema(name = "DiagramUpdateRequest", description = "图表更新请求")
public class DiagramUpdateRequest implements Serializable {

    /**
     * id
     */
    @Schema(description = "图表ID", example = "123456789")
    private Long id;

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

    /**
     * 图表描述
     */
    @Schema(description = "图表描述", example = "前后端分离架构")
    private String description;

    /**
     * 空间id
     */
    @Schema(description = "空间id", example = "12121212")
    private Long spaceId;


    private static final long serialVersionUID = 1L;
}