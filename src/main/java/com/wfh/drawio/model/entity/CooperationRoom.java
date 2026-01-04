package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 协作数据表
 * @author fenghuanwang
 * @TableName cooperation_room
 */
@TableName(value ="cooperation_room")
@Data
public class CooperationRoom {
    /**
     * 房间id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 图表ID
     */
    private Long diagramId;

    /**
     * 加密向量 (可选，看前端加密方案)
     */
    private String iv;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 前端加密后的完整数据 (必须用LONGBLOB)
     */
    private byte[] encryptedData;
}