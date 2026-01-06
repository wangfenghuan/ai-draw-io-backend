package com.wfh.drawio.model.dto.room;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Title: RoomUrlEditRequest
 * @Author wangfenghuan
 * @Package com.wfh.drawio.model.dto.room
 * @Date 2026/1/5 10:38
 * @description:
 */
@Data
public class RoomUrlEditRequest implements Serializable {


    /**
     * 房间id
     */
    @Schema(description = "房间ID", example = "123456789")
    private Long id;


    /**
     * 访问地址
     */
    @Schema(description = "访问地址")
    private String roomUrl;

}
