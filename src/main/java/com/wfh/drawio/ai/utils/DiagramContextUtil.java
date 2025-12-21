package com.wfh.drawio.ai.utils;

import com.wfh.drawio.ai.model.StreamEvent;
import reactor.core.publisher.Sinks;

/**
 * @Title: DiagramContextUtil
 * @Author wangfenghuan
 * @Package com.wfh.drawio.ai.utils
 * @Date 2025/12/21 08:57
 * @description:
 */
public class DiagramContextUtil {

    /**
     * 传递图表id（会话id）
     */
    public static final ScopedValue<String> CONVERSATION_ID = ScopedValue.newInstance();

    /**
     * 用于存放“旁路管道”
     */
    public static final ThreadLocal<Sinks.Many<StreamEvent>> EVENT_SINK = new ThreadLocal<>();

    /**
     * 绑定管道
     * @param sink
     */
    public static void bindSink(Sinks.Many<StreamEvent> sink){
        EVENT_SINK.set(sink);
    }

    /**
     * 发送工具日志，给前端展示工具调用过程
     * @param message
     */
    public static void log(String message){
        Sinks.Many<StreamEvent> sink = EVENT_SINK.get();
        if (sink != null){
            sink.tryEmitNext(StreamEvent.builder()
                    .type("too_call")
                            .content(message)
                    .build());
        }
    }

    /**
     * 发送工具结果，给前端渲染图表
     * @param data
     */
    public static void result(Object data){
        Sinks.Many<StreamEvent> sink = EVENT_SINK.get();
        if (sink != null){
            sink.tryEmitNext(StreamEvent.builder()
                            .type("tool_call_result")
                            .content(data)
                    .build());
        }
    }

    /**
     * 清理资源 (在流结束时调用)
     */
    public static void clear() {
        EVENT_SINK.remove();
    }

    /**
     * 获取当前会话id
     * @return
     */
    public static String getConversationId() {
        return CONVERSATION_ID.isBound() ? CONVERSATION_ID.get() : null;
    }

}
