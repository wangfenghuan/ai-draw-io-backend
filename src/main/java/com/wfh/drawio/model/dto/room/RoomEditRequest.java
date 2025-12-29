package com.wfh.drawio.model.dto.room;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Title: RoomEditRequest
 * @Author wangfenghuan
 * @Package com.wfh.drawio.model.dto.room
 * @Date 2025/12/29 09:47
 * @description: 房间编辑请求
 */
@Data
@Schema(name = "RoomEditRequest", description = "房间编辑请求")
public class RoomEditRequest implements Serializable {

    /**
     * 房间id
     */
    @Schema(description = "房间ID", example = "123456789")
    private Long id;

    /**
     * 房间名称
     */
    @Schema(description = "房间名称", example = "协作编辑房间1")
    private String roomName;

    /**
     * 0代表公开，1代表不公开
     */
    @Schema(description = "是否公开（0公开，1私有）", example = "0")
    private Integer isPublic;

    /**
     * 是否关闭
     */
    @Schema(description = "是否关闭（0开启，1关闭）", example = "0")
    private Integer isOpen;

    /**
     * 访问密码
     */
    @Schema(description = "访问密码", example = "123456")
    private String accessKey;
}
