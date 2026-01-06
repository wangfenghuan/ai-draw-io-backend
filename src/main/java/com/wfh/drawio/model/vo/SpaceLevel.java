package com.wfh.drawio.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 空间级别封装类
 * @author fenghuanwang
 */
@Data
@AllArgsConstructor
@Schema(name = "SpaceLevel", description = "空间级别封装类")
public class SpaceLevel {

    @Schema(description = "级别值", example = "0")
    private int value;

    @Schema(description = "级别文本", example = "普通版")
    private String text;

    @Schema(description = "最大图表数量", example = "100")
    private long maxCount;

    @Schema(description = "最大总大小（字节）", example = "104857600")
    private long maxSize;
}
