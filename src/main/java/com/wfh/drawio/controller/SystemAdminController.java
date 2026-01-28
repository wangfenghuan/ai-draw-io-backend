package com.wfh.drawio.controller;

import com.wfh.drawio.common.BaseResponse;
import com.wfh.drawio.common.ResultUtils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import static com.wfh.drawio.constant.RedisPrefixConstant.*;

/**
 * @author fenghuanwang
 */
@RestController
@RequestMapping("/admin/system")
public class SystemAdminController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 全局禁用AI服务
     * @return 操作结果
     */
    @PostMapping("/shutdown-ai")
    public BaseResponse<String> shutdownAi() {
        // 设置为 false，立即生效
        stringRedisTemplate.opsForValue().set(GLOBAL_AI_SWITCH_KEY, "false");
        return ResultUtils.success("AI 服务已全局禁用");
    }

    /**
     * 全局启用AI服务
     * @return 操作结果
     */
    @PostMapping("/resume-ai")
    public BaseResponse<String> resumeAi() {
        stringRedisTemplate.opsForValue().set(GLOBAL_AI_SWITCH_KEY, "true");
        return ResultUtils.success("AI 服务已全局启用");
    }

    /**
     * 获取全局AI服务状态
     * @return true表示启用，false表示禁用
     */
    @GetMapping("/status-ai")
    public BaseResponse<Boolean> getGlobalAiStatus() {
        String status = stringRedisTemplate.opsForValue().get(GLOBAL_AI_SWITCH_KEY);
        // 默认开启
        boolean isEnabled = !"false".equalsIgnoreCase(status);
        return ResultUtils.success(isEnabled);
    }

    /**
     * 切换指定用户的AI服务权限
     * @param userId 用户ID
     * @param enable true为启用，false为禁用
     * @return 操作结果
     */
    @PostMapping("/user-ai-switch")
    public BaseResponse<String> toggleUserAi(@RequestParam Long userId, @RequestParam Boolean enable) {
        stringRedisTemplate.opsForValue().set(USER_AI_SWITCH_KEY + userId, enable.toString());
        String message = String.format("用户 %d 的AI服务已%s", userId, enable ? "启用" : "禁用");
        return ResultUtils.success(message);
    }

    /**
     * 获取指定用户的AI服务状态
     * @param userId 用户ID
     * @return true表示启用，false表示禁用
     */
    @GetMapping("/user-ai-status")
    public BaseResponse<Boolean> getUserAiStatus(@RequestParam Long userId) {
        String status = stringRedisTemplate.opsForValue().get(USER_AI_SWITCH_KEY + userId);
        // 默认开启
        boolean isEnabled = !"false".equalsIgnoreCase(status);
        return ResultUtils.success(isEnabled);
    }

    /**
     * 获取当前AI使用量
     * @return 当前统计的AI使用量（例如：总耗时）
     */
    @GetMapping("/ai-usage")
    public BaseResponse<String> getAiUsage() {
        String usage = stringRedisTemplate.opsForValue().get(GLOBAL_AI_TOKEN_KEY);
        return ResultUtils.success(usage != null ? usage : "0");
    }
}