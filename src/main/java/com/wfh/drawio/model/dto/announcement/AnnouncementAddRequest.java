package com.wfh.drawio.model.dto.announcement;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 公告创建请求
 *
 * @author wangfenghuan
 */
@Data
@Schema(name = "AnnouncementAddRequest", description = "公告添加请求")
public class AnnouncementAddRequest implements Serializable {

    /**
     * 公告标题
     */
    @Schema(description = "公告标题", example = "系统维护通知")
    private String title;

    /**
     * 公告内容
     */
    @Schema(description = "公告内容", example = "系统将于今晚22:00进行维护，预计持续2小时")
    private String content;

    /**
     * 优先级（1优先级最高，0代表取消公告）
     */
    @Schema(description = "优先级（1优先级最高，0代表取消公告）", example = "1")
    private Integer priority;

    private static final long serialVersionUID = 1L;
}
