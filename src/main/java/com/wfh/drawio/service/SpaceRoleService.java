package com.wfh.drawio.service;

import com.wfh.drawio.model.entity.SysAuthority;
import com.wfh.drawio.model.enums.RoleEnums;

import java.util.List;

/**
 * 空间角色服务接口
 * 处理团队空间角色的权限管理
 *
 * @author wangfenghuan
 */
public interface SpaceRoleService {

    /**
     * 根据空间角色获取对应的权限列表
     *
     * @param spaceRole 空间角色(如 sapce_admin, space_editor, sapce_viewer)
     * @return 权限列表
     */
    List<SysAuthority> getAuthoritiesBySpaceRole(String spaceRole);

    /**
     * 根据角色枚举获取对应的权限列表
     *
     * @param roleEnum 角色枚举
     * @return 权限列表
     */
    List<SysAuthority> getAuthoritiesByRoleEnum(RoleEnums roleEnum);

    /**
     * 检查用户在指定空间是否具有指定权限
     *
     * @param userId 用户ID
     * @param spaceId 空间ID
     * @param authority 权限标识(如 space:diagram:edit)
     * @return 是否具有权限
     */
    boolean hasAuthority(Long userId, Long spaceId, String authority);

    /**
     * 检查用户在指定空间是否具有任意一个指定权限
     *
     * @param userId 用户ID
     * @param spaceId 空间ID
     * @param authorities 权限标识列表
     * @return 是否具有任意一个权限
     */
    boolean hasAnyAuthority(Long userId, Long spaceId, String... authorities);
}
