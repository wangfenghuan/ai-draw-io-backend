package com.wfh.drawio.model.dto.conversion;

import com.wfh.drawio.common.PageRequest;
import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Title: ConversionQueryRequest
 * @Author wangfenghuan
 * @Package com.wfh.drawio.model.dto.conversion
 * @Date 2025/12/22 19:29
 * @description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConversionQueryRequest extends PageRequest implements Serializable {

    /**
     * 对话ID
     */
    private Long id;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 图表ID
     */
    private Long diagramId;

    /**
     * 最新创建时间
     */
    private LocalDateTime lastCreateTime;

    /**
     * 创建用户id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;

}
