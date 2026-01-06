package com.wfh.drawio.model.vo;

import cn.hutool.json.JSONUtil;
import com.wfh.drawio.model.entity.Diagram;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 图表视图
 *
 * @author fenghuanwang
 */
@Data
@Schema(name = "DiagramVO", description = "图表视图对象")
public class DiagramVO implements Serializable {

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
     * 创建用户 id
     */
    @Schema(description = "创建用户ID", example = "10001")
    private Long userId;


    /**
     * 图片url
     */
    @Schema(description = "图片URL", example = "https://example.com/image.png")
    private String pictureUrl;

    /**
     * 矢量图url
     */
    @Schema(description = "矢量图URL", example = "https://example.com/image.svg")
    private String svgUrl;

    /**
     * 图片url
     */
    @Schema(description = "空间id", example = "1111111")
    private Long spaceId;

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
     * 图表描述
     */
    @Schema(description = "图表描述", example = "前后端分离架构")
    private String description;

    /**
     * 图表代码
     */
    @Schema(description = "图表代码", example = "DIAGRAM_20240101_001")
    private String diagramCode;

    /**
     * 封装类转对象
     *
     * @param diagramVO
     * @return
     */
    public static Diagram voToObj(DiagramVO diagramVO) {
        if (diagramVO == null) {
            return null;
        }
        Diagram diagram = new Diagram();
        BeanUtils.copyProperties(diagramVO, diagram);
        return diagram;
    }

    /**
     * 对象转封装类
     *
     * @param diagram
     * @return
     */
    public static DiagramVO objToVo(Diagram diagram) {
        if (diagram == null) {
            return null;
        }
        DiagramVO diagramVO = new DiagramVO();
        BeanUtils.copyProperties(diagram, diagramVO);
        return diagramVO;
    }
}
