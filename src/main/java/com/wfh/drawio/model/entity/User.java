package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户
 *
 * @author wangfenghuan
 * @from wangfenghuan
 */
@TableName(value = "sys_user")
@Data
@Schema(name = "User", description = "用户表")
public class User implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "用户ID", example = "10001")
    private Long id;

    /**
     * 用户账号
     */
    @Schema(description = "用户账号", example = "admin")
    private String userAccount;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码", example = "********")
    private String userPassword;

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
     * 用户头像
     */
    @Schema(description = "用户头像", example = "https://example.com/avatar.jpg")
    private String userAvatar;

    /**
     * 用户简介
     */
    @Schema(description = "用户简介", example = "这是一个用户简介")
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    @Schema(description = "用户角色", example = "user")
    private String userRole;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2024-01-01 10:00:00")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    @Schema(description = "是否删除（0未删除，1已删除）", example = "0")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}