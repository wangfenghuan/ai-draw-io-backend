package com.wfh.drawio.ai.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Title: StreamEvent
 * @Author wangfenghuan
 * @Package com.wfh.drawio.ai.model
 * @Date 2025/12/21 19:39
 * @description:
 */
@Data
@Builder
public class StreamEvent {

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息内容
     */
    private Object content;

}
