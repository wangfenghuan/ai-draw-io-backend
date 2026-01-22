package com.wfh.drawio.model.dto.roommember;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 编辑房间成员请求
 *
 * @author wangfenghuan
 */
@Data
@Schema(name = "RoomMemberEditRequest", description = "编辑房间成员请求")
public class RoomMemberEditRequest implements Serializable {

    /**
     * 房间成员关系 ID
     */
    @Schema(description = "房间成员关系ID", example = "30001", required = true)
    private Long id;

    /**
     * 房间角色：diagram_viewer/diagram_editor/diagram_admin
     */
    @Schema(description = "房间角色", example = "diagram_admin", required = true)
    private String roomRole;

    private static final long serialVersionUID = 1L;
}
