package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 消息对话表
 * @author fenghuanwang
 * @TableName conversion
 */
@TableName(value ="conversion")
@Data
public class Conversion {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 面试id
     */
    private Long diagramId;

    /**
     * (user, 代表用户，ai代表 ai回复)
     */
    private String messageType;

    /**
     * 消息
     */
    private String message;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0未删除，1已删除)
     */
    private Integer isDelete;
}