package com.wfh.drawio.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 文件上传请求
 *
 * @author wangfenghuan
 * @from wangfenghuan
 */
@Data
@Schema(name = "UploadFileRequest", description = "文件上传请求")
public class UploadFileRequest implements Serializable {

    /**
     * 业务
     */
    @Schema(description = "业务类型", example = "avatar")
    private String biz;

    private static final long serialVersionUID = 1L;
}