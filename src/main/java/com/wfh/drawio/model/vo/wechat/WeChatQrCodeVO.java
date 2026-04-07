package com.wfh.drawio.model.vo.wechat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信扫码登录二维码响应
 *
 * @author fenghuanwang
 */
@Data
@Schema(name = "WeChatQrCodeVO", description = "微信扫码登录二维码响应")
public class WeChatQrCodeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 场景ID（用于轮询状态）
     */
    @Schema(description = "场景ID", example = "abc123def456")
    private String sceneId;

    /**
     * 小程序码图片（Base64编码）
     */
    @Schema(description = "小程序码图片Base64", example = "data:image/png;base64,...")
    private String qrCodeBase64;

    /**
     * 二维码过期时间
     */
    @Schema(description = "二维码过期时间", example = "2024-01-01 10:05:00")
    private Date expireTime;
}