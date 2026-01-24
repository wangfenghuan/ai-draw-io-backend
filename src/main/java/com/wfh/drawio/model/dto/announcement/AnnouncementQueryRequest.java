package com.wfh.drawio.model.dto.announcement;

import java.io.Serializable;

import com.wfh.drawio.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公告查询请求
 *
 * @author wangfenghuan
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "AnnouncementQueryRequest", description = "公告查询请求")
public class AnnouncementQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    @Schema(description = "公告ID", example = "10001")
    private Long id;

    /**
     * 公告标题
     */
    @Schema(description = "公告标题", example = "系统维护通知")
    private String title;

    /**
     * 发布人id
     */
    @Schema(description = "发布人ID", example = "10001")
    private Long userId;

    /**
     * 优先级（1优先级最高，0代表取消公告）
     */
    @Schema(description = "优先级（1优先级最高，0代表取消公告）", example = "1")
    private Integer priority;

    private static final long serialVersionUID = 1L;
}
