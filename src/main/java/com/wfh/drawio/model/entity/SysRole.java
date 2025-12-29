package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

/**
 * 角色表
 * @TableName sys_role
 */
@TableName(value ="sys_role")
@Data
@Schema(name = "SysRole", description = "角色表")
public class SysRole {
    /**
     * 角色ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "角色ID", example = "1")
    private Long id;

    /**
     * 角色名称(如:超级管理员)
     */
    @Schema(description = "角色名称", example = "超级管理员")
    private String roleName;

    /**
     * 角色权限字符串(如:admin, editor)
     */
    @Schema(description = "角色权限字符串", example = "admin")
    private String roleKey;

    /**
     * 状态(1:正常 0:停用)
     */
    @Schema(description = "状态(1:正常 0:停用)", example = "1")
    private Integer status;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序", example = "1")
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private Date createTime;

    /**
     * 是否删除
     */
    @TableLogic
    @Schema(description = "是否删除（0未删除，1已删除）", example = "0")
    private Integer isDelete;
}