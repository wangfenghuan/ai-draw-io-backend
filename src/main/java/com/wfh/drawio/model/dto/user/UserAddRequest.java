package com.wfh.drawio.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户创建请求
 *
 * @author wangfenghuan
 * @from wangfenghuan
 */
@Data
@Schema(name = "UserAddRequest", description = "用户添加请求")
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "张三")
    private String userName;

    /**
     * 账号
     */
    @Schema(description = "用户账号", example = "admin")
    private String userAccount;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像", example = "https://example.com/avatar.jpg")
    private String userAvatar;

    /**
     * 用户角色: user, admin
     */
    @Schema(description = "用户角色", example = "user")
    private String userRole;

    private static final long serialVersionUID = 1L;
}