package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

/**
 * 消息对话表
 * @author fenghuanwang
 * @TableName conversion
 */
@TableName(value ="conversion")
@Data
@Schema(name = "Conversion", description = "消息对话表")
public class Conversion {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID", example = "123456789")
    private Long id;

    /**
     * 用户id
     */
    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    /**
     * 图表id
     */
    @Schema(description = "图表ID", example = "20001")
    private Long diagramId;

    /**
     * (user, 代表用户，ai代表 ai回复)
     */
    @Schema(description = "消息类型(user代表用户，ai代表AI回复)", example = "user")
    private String messageType;

    /**
     * 消息
     */
    @Schema(description = "消息内容", example = "你好，这是消息内容")
    private String message;


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
     * 是否删除(0未删除，1已删除)
     */
    @Schema(description = "是否删除(0未删除，1已删除)", example = "0")
    private Integer isDelete;
}