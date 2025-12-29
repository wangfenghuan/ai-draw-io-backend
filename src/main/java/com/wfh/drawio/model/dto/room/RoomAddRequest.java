package com.wfh.drawio.model.dto.room;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Title: RoomAddRequest
 * @Author wangfenghuan
 * @Package com.wfh.drawio.model.dto.room
 * @Date 2025/12/28 11:10
 * @description: 房间添加请求
 */
@Data
@Schema(name = "RoomAddRequest", description = "房间添加请求")
public class RoomAddRequest implements Serializable {

    /**
     * 房间名称
     */
    @Schema(description = "房间名称", example = "协作编辑房间1")
    private String roomName;

    /**
     * 图表id
     */
    @Schema(description = "图表ID", example = "20001")
    private Long diagramId;

}
