package com.wfh.drawio.model.dto.material;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 素材创建请求
 *
 * @author wangfenghuan
 */
@Data
@Schema(name = "MaterialAddRequest", description = "素材添加请求")
public class MaterialAddRequest implements Serializable {

    /**
     * 素材名称
     */
    @Schema(description = "素材名称", example = "流程图模板")
    private String name;

    /**
     * 描述
     */
    @Schema(description = "素材描述", example = "这是一个流程图模板")
    private String description;

    /**
     * 标签（JSON数组字符串）
     */
    @Schema(description = "标签（JSON数组字符串）", example = "[\"流程图\",\"模板\"]")
    private String tags;

    /**
     * 图表代码
     */
    @Schema(description = "图表代码", example = "<mxGraphModel>...</mxGraphModel>")
    private String diagramCode;

    /**
     * png图片地址
     */
    @Schema(description = "PNG图片地址", example = "https://example.com/material.png")
    private String pictureUrl;

    /**
     * svg图片地址
     */
    @Schema(description = "SVG图片地址", example = "https://example.com/material.svg")
    private String svgUrl;

    private static final long serialVersionUID = 1L;
}
