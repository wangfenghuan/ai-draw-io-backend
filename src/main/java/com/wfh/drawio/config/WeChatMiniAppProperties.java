package com.wfh.drawio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序配置属性
 *
 * @author fenghuanwang
 */
@Configuration
@ConfigurationProperties(prefix = "wechat.mini-app")
@Data
public class WeChatMiniAppProperties {

    /**
     * 小程序 AppID
     */
    private String appId;

    /**
     * 小程序 AppSecret
     */
    private String appSecret;

    /**
     * 二维码有效期（分钟）
     */
    private int qrCodeExpireMinutes = 5;

    /**
     * 小程序扫码登录页面路径
     */
    private String scanLoginPage = "pages/login/scan";
}