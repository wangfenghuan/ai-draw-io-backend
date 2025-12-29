package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户和角色关联表
 * @author fenghuanwang
 * @TableName sys_user_role
 */
@TableName(value ="sys_user_role")
@Data
@Schema(name = "SysUserRole", description = "用户和角色关联表")
public class SysUserRole {
    /**
     * 用户ID
     */
    @TableId
    @Schema(description = "用户ID", example = "10001")
    private Long id;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID", example = "1")
    private Long roleId;

    /**
     * 是否删除
     */
    @TableLogic
    @Schema(description = "是否删除（0未删除，1已删除）", example = "0")
    private Integer isDelete;
}