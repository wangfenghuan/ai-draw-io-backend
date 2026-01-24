package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 素材表
 * @author fenghuanwang
 * @TableName material
 */
@TableName(value ="material")
@Data
public class Material {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创作者id
     */
    private Long userId;

    /**
     * 素材名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 标签（JSON数组字符串）
     */
    private String tags;

    /**
     * 图表代码
     */
    private String diagramCode;

    /**
     * png图片地址
     */
    private String pictureUrl;

    /**
     * svg图片地址
     */
    private String svgUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}