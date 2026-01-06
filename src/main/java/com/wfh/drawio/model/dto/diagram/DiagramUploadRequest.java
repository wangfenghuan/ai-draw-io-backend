package com.wfh.drawio.model.dto.diagram;

import com.wfh.drawio.model.dto.file.UploadFileRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Title: DiagramUploadRequest
 * @Author wangfenghuan
 * @Package com.wfh.drawio.model.dto.diagram
 * @Date 2025/12/24 15:57
 * @description: 图表上传请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "DiagramUploadRequest", description = "图表上传请求")
public class DiagramUploadRequest extends UploadFileRequest implements Serializable {

    @Schema(description = "图表ID", example = "123456789")
    private Long diagramId;

    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    @Schema(description = "空间ID", example = "10001")
    private Long spaceId;

}
