package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @author fenghuanwang
 * @TableName room_member
 */
@TableName(value ="room_member")
@Data
public class RoomMember {
    /**
     * 房间成员表id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 成员id
     */
    private Long userId;

    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}