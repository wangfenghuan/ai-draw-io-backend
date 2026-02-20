package com.wfh.drawio.service;

import com.wfh.drawio.ai.client.DrawClient;
import com.wfh.drawio.ai.utils.DiagramContextUtil;
import com.wfh.drawio.model.dto.diagram.CustomChatRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Title: AiService
 * @Author wangfenghuan
 * @Package com.wfh.drawio.service
 * @Date 2025/12/27 12:10
 * @description:
 */
@Service
@Slf4j
public class AiService {

    @NotNull
    public SseEmitter getSseEmitter(CustomChatRequest request, DrawClient drawClient) {
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);
        StringBuilder fullResponse = new StringBuilder();
        String message = request.getMessage();
        String diagramId = request.getDiagramId();
        String modelId = request.getModelId();
        String baseUrl = request.getBaseUrl();
        String apiKey = request.getApiKey();
        // apikey和baseurl是空的话，就调用系统的模型
        if (StringUtils.isEmpty(baseUrl) || StringUtils.isEmpty(apiKey)){
            // 必须先在主线程绑定，否则 contextCapture 抓不到值
            DiagramContextUtil.bindConversationId(diagramId);
            // 如果你需要日志流，这里应该也要绑定 Sink，否则 Tool 里 log 会失效
            // DiagramContextUtil.bindSink(XXX);
            drawClient.doChatStream(message, diagramId, modelId)
                    // 2. 【关键】捕获当前线程的上下文（ThreadLocal），并在后续 Reactor 线程中自动恢复
                    .contextCapture()
                    .subscribe(chunk -> {
                        try{
                            fullResponse.append(chunk);
                            emitter.send(SseEmitter.event().data(chunk));
                        }catch (Exception e){
                            emitter.completeWithError(e);
                        }
                    }, error -> {
                        log.error("流式生成异常", error);
                        try{
                            // 检测 tool argument JSON 截断（XML 过长超出模型输出 token 限制）
                            String errorMsg = buildFriendlyErrorMessage(error);
                            emitter.send(SseEmitter.event().name("error").data(errorMsg));
                        }catch (Exception e){
                            emitter.completeWithError(e);
                        }
                        // 出现异常也要清理资源（虽然 ThreadLocalAccessor 会自动 reset，但主线程的要手动清）
                        DiagramContextUtil.clear();
                    }, () -> {
                        // 流结束
                        emitter.complete();
                        // 清理主线程资源
                        DiagramContextUtil.clear();
                    });
        } else{
            // 必须先在主线程绑定，否则 contextCapture 抓不到值
            DiagramContextUtil.bindConversationId(diagramId);
            // 如果你需要日志流，这里应该也要绑定 Sink，否则 Tool 里 log 会失效
            // DiagramContextUtil.bindSink(XXX);
            drawClient.doCustomChatStream(request)
                    // 2. 【关键】捕获当前线程的上下文（ThreadLocal），并在后续 Reactor 线程中自动恢复
                    .contextCapture()
                    .subscribe(chunk -> {
                        try{
                            fullResponse.append(chunk);
                            emitter.send(SseEmitter.event().data(chunk));
                        }catch (Exception e){
                            emitter.completeWithError(e);
                        }
                    }, error -> {
                        log.error("流式生成异常", error);
                        try{
                            // 检测 tool argument JSON 截断（XML 过长超出模型输出 token 限制）
                            String errorMsg = buildFriendlyErrorMessage(error);
                            emitter.send(SseEmitter.event().name("error").data(errorMsg));
                        }catch (Exception e){
                            emitter.completeWithError(e);
                        }
                        // 出现异常也要清理资源（虽然 ThreadLocalAccessor 会自动 reset，但主线程的要手动清）
                        DiagramContextUtil.clear();
                    }, () -> {
                        // 流结束
                        emitter.complete();
                        // 清理主线程资源
                        DiagramContextUtil.clear();
                    });
        }
        return emitter;
    }

    /**
     * 将底层异常转换为用户可读的错误信息。
     * 特别处理 tool argument JSON 截断场景（AI 生成 XML 过长，超出模型 max_tokens 限制）。
     */
    private String buildFriendlyErrorMessage(Throwable error) {
        String rootMsg = getRootCauseMessage(error);
        // JsonEOFException / 截断特征：'Unexpected end-of-input' 或 'ToolExecutionException'
        if (rootMsg != null && (rootMsg.contains("Unexpected end-of-input")
                || rootMsg.contains("JsonEOFException")
                || (error.getMessage() != null && error.getMessage().contains("Unexpected end-of-input")))) {
            return "生成失败：图表内容过于复杂，AI 输出超出了模型最大 token 限制导致截断。" +
                   "请尝试：① 简化图表要求（减少节点数）② 拆成多次生成 ③ 换用支持更长输出的模型";
        }
        return "生成失败: " + error.getMessage();
    }

    /** 递归获取最根本的异常信息 */
    private String getRootCauseMessage(Throwable t) {
        if (t == null) return null;
        Throwable cause = t.getCause();
        if (cause != null && cause != t) {
            return getRootCauseMessage(cause);
        }
        return t.getMessage();
    }

}
