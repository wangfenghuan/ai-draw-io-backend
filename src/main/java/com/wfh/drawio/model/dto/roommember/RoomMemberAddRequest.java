package com.wfh.drawio.model.dto.roommember;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 添加房间成员请求
 *
 * @author wangfenghuan
 */
@Data
@Schema(name = "RoomMemberAddRequest", description = "添加房间成员请求")
public class RoomMemberAddRequest implements Serializable {

    /**
     * 房间 ID
     */
    @Schema(description = "房间ID", example = "10001", required = true)
    private Long roomId;

    /**
     * 用户 ID
     */
    @Schema(description = "用户ID", example = "20001", required = true)
    private Long userId;

    /**
     * 房间角色：diagram_viewer/diagram_editor/diagram_admin
     */
    @Schema(description = "房间角色", example = "diagram_editor", required = true)
    private String roomRole;

    private static final long serialVersionUID = 1L;
}
