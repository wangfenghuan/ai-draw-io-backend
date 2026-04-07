package com.wfh.drawio.model.dto.wechat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信小程序扫码登录请求
 *
 * @author fenghuanwang
 */
@Data
@Schema(name = "WeChatScanLoginRequest", description = "微信小程序扫码登录请求")
public class WeChatScanLoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 场景ID（从小程序启动参数中获取）
     */
    @Schema(description = "场景ID", example = "abc123def456")
    private String sceneId;

    /**
     * 微信登录code（通过 wx.login 获取）
     */
    @Schema(description = "微信登录code", example = "081xYYGa1...")
    private String code;

    /**
     * 用户昵称（可选，用于首次登录时设置用户名）
     */
    @Schema(description = "用户昵称", example = "微信用户")
    private String nickName;

    /**
     * 用户头像URL（可选，用于首次登录时设置头像）
     */
    @Schema(description = "用户头像URL", example = "https://...")
    private String avatarUrl;
}