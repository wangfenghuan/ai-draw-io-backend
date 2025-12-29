package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色和菜单关联表
 * @author fenghuanwang
 * @TableName sys_role_menu
 */
@TableName(value ="sys_role_menu")
@Data
@Schema(name = "SysRoleMenu", description = "角色和菜单关联表")
public class SysRoleMenu {
    /**
     * 角色ID
     */
    @TableId
    @Schema(description = "角色ID", example = "1")
    private Long id;

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID", example = "100")
    private Long menuId;

    /**
     * 是否删除
     */
    @TableLogic
    @Schema(description = "是否删除（0未删除，1已删除）", example = "0")
    private Integer isDelete;
}