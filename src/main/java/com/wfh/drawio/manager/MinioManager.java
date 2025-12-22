package com.wfh.drawio.manager;

import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.config.MinioClientConfig;
import com.wfh.drawio.exception.BusinessException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @Title: MinioManager
 * @Author wangfenghuan
 * @Package com.wfh.drawio.manager
 * @Date 2025/12/22 18:59
 * @description:
 */
@Component
public class MinioManager {

    @Resource
    private MinioClientConfig clientConfig;

    @Resource
    private MinioClient minioClient;

    public String putObject(String objectName, InputStream inputStream, MultipartFile file){
        try {
            minioClient.putObject(PutObjectArgs.builder()
                            .bucket(clientConfig.getBucketName())
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                        .build());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
        return clientConfig.getEndpoint() + "/" + clientConfig.getBucketName() + "/" + objectName;
    }
}
