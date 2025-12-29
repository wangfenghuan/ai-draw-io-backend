package com.wfh.drawio.model.dto.room;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Title: RoomUpdateRequest
 * @Author wangfenghuan
 * @Package com.wfh.drawio.model.dto.room
 * @Date 2025/12/29 09:45
 * @description: 房间更新请求
 */
@Data
@Schema(name = "RoomUpdateRequest", description = "房间更新请求")
public class RoomUpdateRequest implements Serializable {

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
