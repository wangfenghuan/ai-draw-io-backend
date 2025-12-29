package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色和菜单关联表
 * @author fenghuanwang
 * @TableName sys_role_menu
 */
@TableName(value ="sys_role_menu")
@Data
public class SysRoleMenu {
    /**
     * 角色ID
     */
    @TableId
    private Long id;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}