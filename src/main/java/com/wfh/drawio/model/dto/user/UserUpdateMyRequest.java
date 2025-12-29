package com.wfh.drawio.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新个人信息请求
 *
 * @author wangfenghuan
 * @from wangfenghuan
 */
@Data
@Schema(name = "UserUpdateMyRequest", description = "用户更新个人信息请求")
public class UserUpdateMyRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;
}