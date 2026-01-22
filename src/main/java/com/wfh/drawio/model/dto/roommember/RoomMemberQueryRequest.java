package com.wfh.drawio.model.dto.roommember;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询房间成员请求
 *
 * @author wangfenghuan
 */
@Data
@Schema(name = "RoomMemberQueryRequest", description = "查询房间成员请求")
public class RoomMemberQueryRequest implements Serializable {

    /**
     * 房间成员关系 ID
     */
    @Schema(description = "房间成员关系ID", example = "30001")
    private Long id;

    /**
     * 房间 ID
     */
    @Schema(description = "房间ID", example = "10001")
    private Long roomId;

    /**
     * 用户 ID
     */
    @Schema(description = "用户ID", example = "20001")
    private Long userId;

    /**
     * 房间角色：diagram_viewer/diagram_editor/diagram_admin
     */
    @Schema(description = "房间角色", example = "diagram_editor")
    private String roomRole;

    private static final long serialVersionUID = 1L;
}
