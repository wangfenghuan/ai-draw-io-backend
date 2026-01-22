package com.wfh.drawio.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 房间成员视图对象
 *
 * @author wangfenghuan
 */
@Data
@Schema(name = "RoomMemberVO", description = "房间成员视图对象")
public class RoomMemberVO implements Serializable {

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
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "张三")
    private String userName;

    /**
     * 用户账号
     */
    @Schema(description = "用户账号", example = "zhangsan")
    private String userAccount;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像", example = "https://example.com/avatar.jpg")
    private String userAvatar;

    /**
     * 房间角色
     */
    @Schema(description = "房间角色", example = "diagram_editor")
    private String roomRole;

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

    private static final long serialVersionUID = 1L;
}
