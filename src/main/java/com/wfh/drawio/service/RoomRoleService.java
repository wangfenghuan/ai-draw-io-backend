package com.wfh.drawio.service;

import com.wfh.drawio.model.entity.SysAuthority;
import com.wfh.drawio.model.enums.RoleEnums;

import java.util.List;

/**
 * 房间角色服务接口
 * 处理协作房间的角色权限管理
 *
 * @author wangfenghuan
 */
public interface RoomRoleService {

    /**
     * 根据房间角色获取对应的权限列表
     *
     * @param roomRole 房间角色(如 diagram_admin, diagram_editor, diagram_viewer)
     * @return 权限列表
     */
    List<SysAuthority> getAuthoritiesByRoomRole(String roomRole);

    /**
     * 根据角色枚举获取对应的权限列表
     *
     * @param roleEnum 角色枚举
     * @return 权限列表
     */
    List<SysAuthority> getAuthoritiesByRoleEnum(RoleEnums roleEnum);

    /**
     * 检查用户在指定房间是否具有指定权限
     *
     * @param userId 用户ID
     * @param roomId 房间ID
     * @param authority 权限标识(如 room:diagram:edit)
     * @return 是否具有权限
     */
    boolean hasAuthority(Long userId, Long roomId, String authority);

    /**
     * 检查用户在指定房间是否具有任意一个指定权限
     *
     * @param userId 用户ID
     * @param roomId 房间ID
     * @param authorities 权限标识列表
     * @return 是否具有任意一个权限
     */
    boolean hasAnyAuthority(Long userId, Long roomId, String... authorities);
}
