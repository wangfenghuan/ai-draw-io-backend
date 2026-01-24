package com.wfh.drawio.model.dto.spaceuser;

import lombok.Data;

import java.io.Serializable;

/**
 * @Title: SpaceUserDeleteRequest
 * @Author wangfenghuan
 * @Package com.wfh.drawio.model.dto.spaceuser
 * @Date 2026/1/24 13:29
 * @description:
 */
@Data
public class SpaceUserDeleteRequest implements Serializable {

    /**
     * 空间id
     */
    private Long spaceId;

    /**
     * 用户id
     */
    private Long userId;

}
