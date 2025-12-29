package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

/**
 * 协同编辑增量表
 * @author fenghuanwang
 * @TableName room_updates
 */
@TableName(value ="room_updates")
@Data
@Schema(name = "RoomUpdates", description = "协同编辑增量表")
public class RoomUpdates {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID", example = "123456789")
    private Long id;

    /**
     * 房间/文件ID
     */
    @Schema(description = "房间/文件ID", example = "10001")
    private Long roomName;

    /**
     * 产生时间
     */
    @Schema(description = "产生时间", example = "2024-01-01 10:00:00")
    private Date createTime;

    /**
     * Yjs二进制增量数据(通常很小)
     */
    @Schema(description = "Yjs二进制增量数据")
    private byte[] updateData;

    /**
     * 是否删除（0未删除，1删除）
     */
    @TableLogic
    @Schema(description = "是否删除（0未删除，1删除）", example = "0")
    private Integer isDelete;
}