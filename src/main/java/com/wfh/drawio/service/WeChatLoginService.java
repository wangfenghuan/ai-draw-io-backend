package com.wfh.drawio.service;

import com.wfh.drawio.model.dto.wechat.WeChatScanLoginRequest;
import com.wfh.drawio.model.vo.wechat.WeChatQrCodeVO;
import com.wfh.drawio.model.vo.wechat.WeChatLoginStatusVO;

/**
 * 微信扫码登录服务
 *
 * @author fenghuanwang
 */
public interface WeChatLoginService {

    /**
     * 生成扫码登录二维码
     * 生成 scene_id，调用微信 API 获取小程序码，存入 Redis
     *
     * @return 二维码信息（包含 sceneId、图片Base64、过期时间）
     */
    WeChatQrCodeVO generateQrCode();

    /**
     * 查询登录状态
     *
     * @param sceneId 场景ID
     * @return 登录状态（waiting/scanned/success/expired）
     */
    WeChatLoginStatusVO queryLoginStatus(String sceneId);

    /**
     * 小程序扫码确认登录
     * 用 code 换取 openid，同步用户，更新 Redis 状态
     *
     * @param request 扫码请求（包含 sceneId、code）
     */
    void scanLogin(WeChatScanLoginRequest request);
}