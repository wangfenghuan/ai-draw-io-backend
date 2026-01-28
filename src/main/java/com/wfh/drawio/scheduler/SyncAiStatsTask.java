package com.wfh.drawio.scheduler;

import com.wfh.drawio.model.entity.AiReqLog;
import com.wfh.drawio.service.AiReqLogService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.wfh.drawio.constant.RedisPrefixConstant.GLOBAL_AI_TOKEN_KEY;

/**
 * AI 相关统计数据定时任务
 *
 * @author fenghuanwang
 */
@Component
@Slf4j
public class SyncAiStatsTask {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AiReqLogService aiReqLogService;

    /**
     * 应用启动时，重置AI使用量计数器
     */
    @PostConstruct
    public void resetAiUsage() {
        stringRedisTemplate.opsForValue().set(GLOBAL_AI_TOKEN_KEY, "0");
        log.info("应用启动，重置AI使用量计数器。");
    }

    /**
     * 每天0点执行，同步前一天Redis的AI使用数据到MySQL
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void syncAiUsageToDatabase() {
        // 1. 从 Redis 获取前一天的总耗时
        String totalTimeStr = stringRedisTemplate.opsForValue().get(GLOBAL_AI_TOKEN_KEY);

        if (totalTimeStr != null && !totalTimeStr.isEmpty()) {
            try {
                // 2. 将耗时转换为数值
                double totalTime = Double.parseDouble(totalTimeStr);
                int usageCount = (int) Math.round(totalTime); // 四舍五入为整数

                // 3. 如果使用量大于0，则存入数据库
                if (usageCount > 0) {
                    AiReqLog aiReqLog = new AiReqLog();
                    aiReqLog.setCount(usageCount);
                    boolean saved = aiReqLogService.save(aiReqLog);
                    if (saved) {
                        log.info("成功将前一天的AI使用数据同步到数据库，总耗时：{}秒", usageCount);
                    } else {
                        log.error("无法将AI使用数据同步到数据库。");
                        // 如果无法保存，则不重置计数器，以便下次重试
                        return;
                    }
                }
                // 4. 成功处理后，重置 Redis 计数器为0，以便开始新一天的计数
                stringRedisTemplate.opsForValue().set(GLOBAL_AI_TOKEN_KEY, "0");

            } catch (NumberFormatException e) {
                log.error("无法解析AI使用数据，Redis中的值为：'{}'。错误：{}", totalTimeStr, e.getMessage());
                // 解析失败也重置，防止错误数据持续存在
                stringRedisTemplate.opsForValue().set(GLOBAL_AI_TOKEN_KEY, "0");
            }
        } else {
            // 如果键不存在或为空，也设置为0，确保第二天有个干净的开始
            stringRedisTemplate.opsForValue().set(GLOBAL_AI_TOKEN_KEY, "0");
            log.info("Redis中未找到AI使用数据，已将计数器设置为0。");
        }
    }
}
