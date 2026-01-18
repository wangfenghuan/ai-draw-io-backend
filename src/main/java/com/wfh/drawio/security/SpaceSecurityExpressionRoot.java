package com.wfh.drawio.security;

import lombok.Setter;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * 自定义 Security Expression Root
 * 扩展 Spring Security 默认的表达式，支持空间权限和房间权限检查
 * 必须实现 MethodSecurityExpressionOperations 以支持方法级别的安全校验
 *
 * @author wangfenghuan
 */
public class SpaceSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    @Setter
    private SpaceSecurityService spaceSecurityService;
    @Setter
    private RoomSecurityService roomSecurityService;
    private Object filterObject;
    private Object returnObject;
    private Object target;

    public SpaceSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    // ==================== 团队空间权限检查 ====================

    /**
     * 检查当前用户在指定空间是否具有指定权限
     */
    public boolean hasSpaceAuthority(Long spaceId, String authority) {
        if (spaceSecurityService == null) {
            return false;
        }
        return spaceSecurityService.hasSpaceAuthority(spaceId, authority);
    }

    /**
     * 检查当前用户在指定空间是否具有任意一个指定权限
     */
    public boolean hasAnySpaceAuthority(Long spaceId, String... authorities) {
        if (spaceSecurityService == null) {
            return false;
        }
        return spaceSecurityService.hasAnySpaceAuthority(spaceId, authorities);
    }

    // ==================== 协作房间权限检查 ====================

    /**
     * 检查当前用户在指定房间是否具有指定权限
     * 使用示例: @PreAuthorize("hasRoomAuthority(#roomId, 'room:diagram:edit')")
     *
     * @param roomId 房间ID
     * @param authority 权限标识
     * @return 是否具有权限
     */
    public boolean hasRoomAuthority(Long roomId, String authority) {
        if (roomSecurityService == null) {
            return false;
        }
        return roomSecurityService.hasRoomAuthority(roomId, authority);
    }

    /**
     * 检查当前用户在指定房间是否具有任意一个指定权限
     * 使用示例: @PreAuthorize("hasAnyRoomAuthority(#roomId, 'room:diagram:edit', 'room:diagram:view')")
     *
     * @param roomId 房间ID
     * @param authorities 权限标识数组
     * @return 是否具有任意一个权限
     */
    public boolean hasAnyRoomAuthority(Long roomId, String... authorities) {
        if (roomSecurityService == null) {
            return false;
        }
        return roomSecurityService.hasAnyRoomAuthority(roomId, authorities);
    }

    // ========== MethodSecurityExpressionOperations 接口实现 ==========

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    /**
     * 用于处理 "this" 引用
     */
    public void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return target;
    }
}