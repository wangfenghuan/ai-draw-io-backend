package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

/**
 * 菜单权限表
 * @author fenghuanwang
 * @TableName sys_menu
 */
@TableName(value ="sys_menu")
@Data
@Schema(name = "SysMenu", description = "菜单权限表")
public class SysMenu {
    /**
     * 菜单ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "菜单ID", example = "1")
    private Long id;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称", example = "系统管理")
    private String menuName;

    /**
     * 父菜单ID
     */
    @Schema(description = "父菜单ID", example = "0")
    private Long parentId;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序", example = "1")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @Schema(description = "路由地址", example = "/system")
    private String path;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径", example = "system/index")
    private String component;

    /**
     * 类型(M:目录 C:菜单 F:按钮)
     */
    @Schema(description = "类型(M:目录 C:菜单 F:按钮)", example = "M")
    private String menuType;

    /**
     * 权限标识(如: sys:user:add)
     */
    @Schema(description = "权限标识", example = "sys:user:add")
    private String perms;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标", example = "system")
    private String icon;

    /**
     * 是否显示(1:显示 0:隐藏)
     */
    @Schema(description = "是否显示(1:显示 0:隐藏)", example = "1")
    private Integer visible;

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