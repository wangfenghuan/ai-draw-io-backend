package com.wfh.drawio.model.dto.room;

import lombok.Data;

import java.io.Serializable;

/**
 * @Title: RoomAddRequest
 * @Author wangfenghuan
 * @Package com.wfh.drawio.model.dto.room
 * @Date 2025/12/28 11:10
 * @description:
 */
@Data
public class RoomAddRequest implements Serializable {

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 图表id
     */
    private Long diagramId;

}
