package com.wfh.drawio.aop;

import com.wfh.drawio.annotation.AiFeature;
import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.exception.BusinessException;
import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

import static com.wfh.drawio.constant.RedisPrefixConstant.*;

/**
 * @author fenghuanwang
 */
@Aspect
@Component
public class AiCircuitBreakerAspect {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;


    @Around("@annotation(aiFeature)")
    public Object checkGlobalSwitch(ProceedingJoinPoint joinPoint, AiFeature aiFeature) throws Throwable {
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(httpServletRequest);
        // 1. 从 Redis 读取开关状态
        // 如果 Key 不存在，默认认为是开启的 (true)，或者你可以设计为默认关闭
        String status = stringRedisTemplate.opsForValue().get(GLOBAL_AI_SWITCH_KEY);
        
        // 2. 如果状态是 "false" 或 "off"，直接抛出异常，阻止后续逻辑执行
        if ("false".equalsIgnoreCase(status) || "off".equalsIgnoreCase(status)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI服务维护中，请使用自定义llm");
        }
        // 全局ai是启用的情况下，校验某个用户是否启用ai功能
        String userKey = USER_AI_SWITCH_KEY + loginUser.getId().toString();
        String userStatus = stringRedisTemplate.opsForValue().get(userKey);
        if ("false".equalsIgnoreCase(userStatus) || "off".equalsIgnoreCase(userStatus)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您的AI服务被禁用，请联系管理员");
        }
        // 3. 在这里顺便做全局 Token 消耗监控(因为是流式响应的，所以统计本次ai回复的时长反应token消耗)
        StopWatch stopWatch = new StopWatch();
        // 开始计时
        stopWatch.start();
        // 执行原方法
        Object result = joinPoint.proceed();
        // 停止计时, 计算总时间
        stopWatch.stop();
        double totalTime = stopWatch.getTotalTime(TimeUnit.SECONDS);
        // 总耗时写入Redis
        stringRedisTemplate.opsForValue().increment(GLOBAL_AI_TOKEN_KEY, totalTime);
        return result;
    }
}