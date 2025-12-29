package com.wfh.drawio.model.dto.user;


import java.io.Serializable;

import com.wfh.drawio.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询请求
 *
 * @author wangfenghuan
 * @from wangfenghuan
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "UserQueryRequest", description = "用户查询请求")
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    @Schema(description = "用户ID", example = "10001")
    private Long id;

    /**
     * 开放平台id
     */
    @Schema(description = "开放平台ID", example = "UnionId123456")
    private String unionId;

    /**
     * 公众号openId
     */
    @Schema(description = "公众号OpenID", example = "OpenId123456")
    private String mpOpenId;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "张三")
    private String userName;

    /**
     * 简介
     */
    @Schema(description = "用户简介", example = "这是一个用户简介")
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    @Schema(description = "用户角色", example = "user")
    private String userRole;

    private static final long serialVersionUID = 1L;
}