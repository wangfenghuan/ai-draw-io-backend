package com.wfh.drawio.model.dto.conversion;

import com.wfh.drawio.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * @description: 对话查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "ConversionQueryRequest", description = "对话查询请求")
public class ConversionQueryRequest extends PageRequest implements Serializable {

    /**
     * 对话ID
     */
    @Schema(description = "对话ID", example = "123456789")
    private Long id;

    /**
     * 消息内容
     */
    @Schema(description = "消息内容", example = "你好，这是消息内容")
    private String message;

    /**
     * 消息类型
     */
    @Schema(description = "消息类型", example = "user")
    private String messageType;

    /**
     * 图表ID
     */
    @Schema(description = "图表ID", example = "20001")
    private Long diagramId;

    /**
     * 最新创建时间
     */
    @Schema(description = "最新创建时间", example = "2024-01-01 10:00:00")
    private LocalDateTime lastCreateTime;

    /**
     * 创建用户id
     */
    @Schema(description = "创建用户ID", example = "10001")
    private Long userId;

    private static final long serialVersionUID = 1L;

}
