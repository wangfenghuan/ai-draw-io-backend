package com.wfh.drawio.model.dto.material;

import java.io.Serializable;

import com.wfh.drawio.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 素材查询请求
 *
 * @author wangfenghuan
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "MaterialQueryRequest", description = "素材查询请求")
public class MaterialQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    @Schema(description = "素材ID", example = "10001")
    private Long id;

    /**
     * 素材名称
     */
    @Schema(description = "素材名称", example = "流程图模板")
    private String name;

    /**
     * 标签（JSON数组字符串）
     */
    @Schema(description = "标签（JSON数组字符串）", example = "流程图")
    private String tags;

    /**
     * 创作者id
     */
    @Schema(description = "创作者ID", example = "10001")
    private Long userId;

    private static final long serialVersionUID = 1L;
}
