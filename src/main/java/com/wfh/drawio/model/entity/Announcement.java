package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * Announcement 公告表
 * @author fenghuanwang
 * @TableName announcement
 */
@TableName(value ="announcement")
@Data
public class Announcement {
    /**
     * 公告主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发布人id
     */
    private Long userId;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

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
    private Long isDelete;

    /**
     * 优先级（（1优先级最高）0,代表取消公告）
     */
    private Integer priority;
}