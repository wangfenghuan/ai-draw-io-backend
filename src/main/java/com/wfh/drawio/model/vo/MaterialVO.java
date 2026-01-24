package com.wfh.drawio.model.vo;

import com.wfh.drawio.model.entity.Material;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 素材视图
 *
 * @author fenghuanwang
 */
@Data
@Schema(name = "MaterialVO", description = "素材视图对象")
public class MaterialVO implements Serializable {

    /**
     * id
     */
    @Schema(description = "素材ID", example = "123456789")
    private Long id;

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

    /**
     * 创建用户 id
     */
    @Schema(description = "创建用户ID", example = "10001")
    private Long userId;

    /**
     * 创建用户信息
     */
    @Schema(description = "创建用户信息")
    private UserVO userVO;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2024-01-01 10:00:00")
    private Date updateTime;

    /**
     * 封装类转对象
     *
     * @param materialVO
     * @return
     */
    public static Material voToObj(MaterialVO materialVO) {
        if (materialVO == null) {
            return null;
        }
        Material material = new Material();
        BeanUtils.copyProperties(materialVO, material);
        return material;
    }

    /**
     * 对象转封装类
     *
     * @param material
     * @return
     */
    public static MaterialVO objToVo(Material material) {
        if (material == null) {
            return null;
        }
        MaterialVO materialVO = new MaterialVO();
        BeanUtils.copyProperties(material, materialVO);
        return materialVO;
    }
}
