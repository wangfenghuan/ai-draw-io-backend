package com.wfh.drawio.model.dto.diagram;

import com.wfh.drawio.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询图表请求
 *
 * @author fenghuanwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "DiagramQueryRequest", description = "图表查询请求")
public class DiagramQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    @Schema(description = "图表ID", example = "123456789")
    private Long id;


    /**
     * 搜索词
     */
    @Schema(description = "搜索关键词", example = "架构")
    private String searchText;

    /**
     * 标题
     */
    @Schema(description = "图表标题", example = "系统架构图")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "图表代码", example = "DIAGRAM_20240101_001")
    private String diagramCode;


    /**
     * 空间 id
     */
    @Schema(description = "空间 id")
    private Long spaceId;

    /**
     * 是否只查询 spaceId 为 null 的数据
     */
    @Schema(description = "是否只查询 spaceId 为 null 的数据")
    private boolean nullSpaceId;



    /**
     * 创建用户 id
     */
    @Schema(description = "创建用户ID", example = "10001")
    private Long userId;

    private static final long serialVersionUID = 1L;
}