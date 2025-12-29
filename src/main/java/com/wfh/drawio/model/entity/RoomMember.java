package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author fenghuanwang
 * @TableName room_member
 */
@TableName(value ="room_member")
@Data
@Schema(name = "RoomMember", description = "房间成员表")
public class RoomMember {
    /**
     * 房间成员表id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "房间成员表ID", example = "123456789")
    private Long id;

    /**
     * 成员id
     */
    @Schema(description = "成员ID", example = "10001")
    private Long userId;

    /**
     * 房间id
     */
    @Schema(description = "房间ID", example = "20001")
    private Long roomId;
    /**
     * 是否删除
     */
    @TableLogic
    @Schema(description = "是否删除（0未删除，1已删除）", example = "0")
    private Integer isDelete;

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
}