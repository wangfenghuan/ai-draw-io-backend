package com.wfh.drawio.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新请求
 *
 * @author wangfenghuan
 * @from wangfenghuan
 */
@Data
@Schema(name = "UserUpdateRequest", description = "用户更新请求")
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    @Schema(description = "用户ID", example = "10001")
    private Long id;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "张三")
    private String userName;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像", example = "https://example.com/avatar.jpg")
    private String userAvatar;

    /**
     * 简介
     */
    @Schema(description = "用户简介", example = "这是一个用户简介")
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    @Schema(description = "用户角色", example = "user")
    private String userRole;

    private static final long serialVersionUID = 1L;
}