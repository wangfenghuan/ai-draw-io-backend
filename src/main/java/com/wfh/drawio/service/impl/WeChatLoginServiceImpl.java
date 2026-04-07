package com.wfh.drawio.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTPayload;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.config.WeChatMiniAppProperties;
import com.wfh.drawio.constant.RedisPrefixConstant;
import com.wfh.drawio.exception.BusinessException;
import com.wfh.drawio.model.dto.wechat.WeChatScanLoginRequest;
import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.model.vo.LoginUserVO;
import com.wfh.drawio.model.vo.wechat.WeChatLoginStatusVO;
import com.wfh.drawio.model.vo.wechat.WeChatQrCodeVO;
import com.wfh.drawio.service.UserService;
import com.wfh.drawio.service.WeChatLoginService;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 微信扫码登录服务实现
 *
 * @author fenghuanwang
 */
@Service
@Slf4j
public class WeChatLoginServiceImpl implements WeChatLoginService {

    @Resource
    private WeChatMiniAppProperties weChatMiniAppProperties;

    @Resource
    @Lazy
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ObjectMapper objectMapper;

    // 微信 API 地址
    private static final String WECHAT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String WECHAT_QRCODE_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit";
    private static final String WECHAT_CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    // 登录状态
    private static final String STATUS_WAITING = "waiting";
    private static final String STATUS_SCANNED = "scanned";
    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_EXPIRED = "expired";

    // JWT 密钥
    private static final String JWT_SECRET = "wfh-drawio-jwt-secret";

    @Override
    public WeChatQrCodeVO generateQrCode() {
        try {
            // 1. 生成唯一的 sceneId
            String sceneId = RandomUtil.randomString(16);

            // 2. 获取 access_token
            String accessToken = getAccessToken();

            // 3. 调用微信 API 获取小程序码
            String qrCodeBase64 = getMiniAppQrCode(accessToken, sceneId);

            // 4. 存入 Redis（状态为 waiting）
            String statusKey = RedisPrefixConstant.WECHAT_SCAN_LOGIN_STATUS + sceneId;
            int expireMinutes = weChatMiniAppProperties.getQrCodeExpireMinutes();

            // 存储状态信息（JSON 格式）
            Map<String, String> statusData = new HashMap<>();
            statusData.put("status", STATUS_WAITING);
            statusData.put("createTime", String.valueOf(System.currentTimeMillis()));
            stringRedisTemplate.opsForValue().set(statusKey, objectMapper.writeValueAsString(statusData),
                    expireMinutes, TimeUnit.MINUTES);

            // 5. 构建响应
            WeChatQrCodeVO vo = new WeChatQrCodeVO();
            vo.setSceneId(sceneId);
            vo.setQrCodeBase64(qrCodeBase64);
            vo.setExpireTime(new Date(System.currentTimeMillis() + expireMinutes * 60 * 1000));

            log.info("生成微信扫码登录二维码成功，sceneId: {}", sceneId);
            return vo;

        } catch (Exception e) {
            log.error("生成微信扫码登录二维码失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成二维码失败: " + e.getMessage());
        }
    }

    @Override
    public WeChatLoginStatusVO queryLoginStatus(String sceneId) {
        if (sceneId == null || sceneId.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "sceneId 不能为空");
        }

        String statusKey = RedisPrefixConstant.WECHAT_SCAN_LOGIN_STATUS + sceneId;
        String statusJson = stringRedisTemplate.opsForValue().get(statusKey);

        WeChatLoginStatusVO vo = new WeChatLoginStatusVO();

        if (statusJson == null) {
            // 二维码已过期或不存在
            vo.setStatus(STATUS_EXPIRED);
            vo.setMessage("二维码已过期，请重新获取");
            return vo;
        }

        try {
            Map<String, String> statusData = objectMapper.readValue(statusJson, Map.class);
            String status = statusData.get("status");

            vo.setStatus(status);

            switch (status) {
                case STATUS_WAITING:
                    vo.setMessage("等待用户扫码");
                    break;
                case STATUS_SCANNED:
                    vo.setMessage("已扫码，等待确认");
                    break;
                case STATUS_SUCCESS:
                    // 登录成功，返回 token 和用户信息
                    String token = statusData.get("token");
                    String userId = statusData.get("userId");
                    vo.setToken(token);
                    vo.setMessage("登录成功");

                    // 获取用户信息
                    if (userId != null) {
                        User user = userService.getById(Long.parseLong(userId));
                        if (user != null) {
                            LoginUserVO loginUserVO = userService.getLoginUserVO(user);
                            loginUserVO.setToken(token);
                            vo.setUserInfo(loginUserVO);
                        }
                    }
                    break;
                case STATUS_EXPIRED:
                    vo.setMessage("二维码已过期");
                    break;
                default:
                    vo.setMessage("未知状态");
            }

            return vo;

        } catch (Exception e) {
            log.error("解析登录状态失败", e);
            vo.setStatus(STATUS_EXPIRED);
            vo.setMessage("状态解析失败");
            return vo;
        }
    }

    @Override
    public void scanLogin(WeChatScanLoginRequest request) {
        if (request == null || request.getSceneId() == null || request.getCode() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不完整");
        }

        String sceneId = request.getSceneId();
        String code = request.getCode();

        // 1. 检查 sceneId 是否有效
        String statusKey = RedisPrefixConstant.WECHAT_SCAN_LOGIN_STATUS + sceneId;
        String statusJson = stringRedisTemplate.opsForValue().get(statusKey);

        if (statusJson == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "二维码已过期或不存在");
        }

        try {
            Map<String, String> statusData = objectMapper.readValue(statusJson, Map.class);
            String currentStatus = statusData.get("status");

            if (!STATUS_WAITING.equals(currentStatus) && !STATUS_SCANNED.equals(currentStatus)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "二维码状态异常");
            }

            // 2. 用 code 换取 openid
            WeChatSessionResult sessionResult = code2Session(code);
            String openid = sessionResult.getOpenid();

            if (openid == null || openid.isEmpty()) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取 openid 失败");
            }

            log.info("微信小程序扫码登录，sceneId: {}, openid: {}", sceneId, openid);

            // 3. 同步或创建用户
            User user = syncOrCreateUser(openid, request.getNickName(), request.getAvatarUrl());

            // 4. 生成 JWT Token
            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", user.getId());
            payload.put(JWTPayload.EXPIRES_AT, System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30); // 30天过期
            String token = JWTUtil.createToken(payload, JWT_SECRET.getBytes());

            // 5. 更新 Redis 状态为 success
            statusData.put("status", STATUS_SUCCESS);
            statusData.put("token", token);
            statusData.put("userId", user.getId().toString());
            statusData.put("openid", openid);

            // 获取剩余过期时间
            Long expireTime = stringRedisTemplate.getExpire(statusKey, TimeUnit.MINUTES);
            if (expireTime != null && expireTime > 0) {
                stringRedisTemplate.opsForValue().set(statusKey, objectMapper.writeValueAsString(statusData),
                        expireTime, TimeUnit.MINUTES);
            } else {
                // 设置一个较短的有效期，确保 PC 端能获取到结果
                stringRedisTemplate.opsForValue().set(statusKey, objectMapper.writeValueAsString(statusData),
                        5, TimeUnit.MINUTES);
            }

            log.info("微信小程序扫码登录成功，userId: {}", user.getId());

        } catch (Exception e) {
            log.error("微信小程序扫码登录失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败: " + e.getMessage());
        }
    }

    /**
     * 获取微信 access_token
     */
    private String getAccessToken() {
        // 尝试从缓存获取
        String cacheKey = "wechat:access_token:" + weChatMiniAppProperties.getAppId();
        String cachedToken = stringRedisTemplate.opsForValue().get(cacheKey);

        if (cachedToken != null && !cachedToken.isEmpty()) {
            return cachedToken;
        }

        // 调用微信 API 获取
        String url = String.format("%s?grant_type=client_credential&appid=%s&secret=%s",
                WECHAT_ACCESS_TOKEN_URL,
                weChatMiniAppProperties.getAppId(),
                weChatMiniAppProperties.getAppSecret());

        String response = HttpUtil.get(url);

        try {
            Map<String, Object> result = objectMapper.readValue(response, Map.class);
            if (result.containsKey("errcode") && (Integer) result.get("errcode") != 0) {
                log.error("获取 access_token 失败: {}", response);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取 access_token 失败");
            }

            String accessToken = (String) result.get("access_token");
            Integer expiresIn = (Integer) result.get("expires_in");

            // 缓存 access_token，提前 5 分钟过期
            stringRedisTemplate.opsForValue().set(cacheKey, accessToken,
                    expiresIn - 300, TimeUnit.SECONDS);

            log.info("获取微信 access_token 成功，有效期: {} 秒", expiresIn);
            return accessToken;

        } catch (Exception e) {
            log.error("解析 access_token 响应失败: {}", response, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取 access_token 失败");
        }
    }

    /**
     * 获取小程序码
     */
    private String getMiniAppQrCode(String accessToken, String sceneId) {
        String url = String.format("%s?access_token=%s", WECHAT_QRCODE_URL, accessToken);

        // 构建请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("scene", sceneId);
        params.put("page", weChatMiniAppProperties.getScanLoginPage());
        params.put("width", 280);
        params.put("auto_color", false);
        params.put("line_color", Map.of("r", 0, "g", 0, "b", 0));
        params.put("is_hyaline", false);

        try {
            HttpResponse httpResponse = HttpRequest.post(url)
                    .body(objectMapper.writeValueAsString(params))
                    .execute();
            byte[] imageBytes = httpResponse.bodyBytes();

            // 检查返回的是否是错误信息（JSON）
            if (imageBytes.length < 1000) {
                // 可能是错误响应，尝试解析
                try {
                    String errorResponse = new String(imageBytes);
                    Map<String, Object> error = objectMapper.readValue(errorResponse, Map.class);
                    if (error.containsKey("errcode")) {
                        log.error("获取小程序码失败: {}", errorResponse);
                        throw new BusinessException(ErrorCode.SYSTEM_ERROR,
                                "获取小程序码失败: " + error.get("errmsg"));
                    }
                } catch (Exception ignored) {
                    // 不是 JSON，是正常图片
                }
            }

            // 返回 Base64 编码的图片
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);

        } catch (Exception e) {
            log.error("获取小程序码失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取小程序码失败: " + e.getMessage());
        }
    }

    /**
     * 用 code 换取 openid 和 session_key
     */
    private WeChatSessionResult code2Session(String code) {
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                WECHAT_CODE2SESSION_URL,
                weChatMiniAppProperties.getAppId(),
                weChatMiniAppProperties.getAppSecret(),
                code);

        String response = HttpUtil.get(url);

        try {
            Map<String, Object> result = objectMapper.readValue(response, Map.class);
            if (result.containsKey("errcode") && (Integer) result.get("errcode") != 0) {
                log.error("code2Session 失败: {}", response);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,
                        "微信登录失败: " + result.get("errmsg"));
            }

            WeChatSessionResult sessionResult = new WeChatSessionResult();
            sessionResult.setOpenid((String) result.get("openid"));
            sessionResult.setSessionKey((String) result.get("session_key"));
            sessionResult.setUnionid((String) result.get("unionid"));

            return sessionResult;

        } catch (Exception e) {
            log.error("解析 code2Session 响应失败: {}", response, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "微信登录失败");
        }
    }

    /**
     * 同步或创建用户
     *
     * @param openid      小程序 OpenID
     * @param nickName    用户昵称
     * @param avatarUrl   用户头像
     */
    private User syncOrCreateUser(String openid, String nickName, String avatarUrl) {
        // 根据 openid 查找用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getMiniAppOpenId, openid);
        User existingUser = userService.getOne(wrapper);

        if (existingUser != null) {
            // 已存在用户，更新用户信息（如果提供了新的昵称或头像）
            boolean needUpdate = false;
            if (nickName != null && !nickName.equals(existingUser.getUserName())) {
                existingUser.setUserName(nickName);
                needUpdate = true;
            }
            if (avatarUrl != null && !avatarUrl.equals(existingUser.getUserAvatar())) {
                existingUser.setUserAvatar(avatarUrl);
                needUpdate = true;
            }
            if (needUpdate) {
                userService.updateById(existingUser);
                log.info("更新微信用户信息: userId={}", existingUser.getId());
            }
            return existingUser;
        }

        // 创建新用户
        User newUser = new User();
        newUser.setUserAccount("wechat_" + RandomUtil.randomString(8));
        newUser.setMiniAppOpenId(openid);
        newUser.setUserPassword(passwordEncoder.encode(RandomUtil.randomString(16)));
        newUser.setUserName(nickName != null ? nickName : "微信用户" + RandomUtil.randomString(4));
        newUser.setUserAvatar(avatarUrl);
        newUser.setUserRole("user");
        // 为新用户生成专属邀请码（6位随机字符串）
        newUser.setInviteCode(RandomUtil.randomString(6));

        boolean saved = userService.save(newUser);
        if (!saved) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建微信用户失败");
        }

        log.info("创建微信用户成功: userId={}, openid={}", newUser.getId(), openid);
        return newUser;
    }

    /**
     * 微信 code2Session 返回结果
     */
    @Data
    private static class WeChatSessionResult {
        private String openid;
        private String sessionKey;
        private String unionid;
    }
}