package com.wfh.drawio.model.vo.wechat;

import com.wfh.drawio.model.vo.LoginUserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信扫码登录状态响应
 *
 * @author fenghuanwang
 */
@Data
@Schema(name = "WeChatLoginStatusVO", description = "微信扫码登录状态响应")
public class WeChatLoginStatusVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录状态
     * waiting: 待扫码
     * scanned: 已扫码待确认
     * success: 登录成功
     * expired: 已过期
     */
    @Schema(description = "登录状态", example = "waiting")
    private String status;

    /**
     * 登录凭证（仅 success 状态时返回）
     */
    @Schema(description = "登录凭证", example = "eyJhbGciOiJI...")
    private String token;

    /**
     * 用户信息（仅 success 状态时返回）
     */
    @Schema(description = "用户信息")
    private LoginUserVO userInfo;

    /**
     * 状态说明
     */
    @Schema(description = "状态说明", example = "等待用户扫码")
    private String message;
}