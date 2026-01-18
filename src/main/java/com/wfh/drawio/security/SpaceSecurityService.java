package com.wfh.drawio.security;

import com.wfh.drawio.service.SpaceRoleService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 空间权限服务
 * 用于在 Security 表达式中调用
 *
 * @author wangfenghuan
 */
@Component
public class SpaceSecurityService {

    @Resource
    private SpaceRoleService spaceRoleService;

    /**
     * 检查用户在指定空间是否具有指定权限
     *
     * @param spaceId   空间ID
     * @param authority 权限标识
     * @return 是否具有权限
     */
    public boolean hasSpaceAuthority(Long spaceId, String authority) {
        if (spaceId == null || authority == null) {
            return false;
        }

        // 获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // 从认证信息中获取用户ID
        Object principal = authentication.getPrincipal();
        Long userId = extractUserId(principal);

        if (userId == null) {
            return false;
        }

        return spaceRoleService.hasAuthority(userId, spaceId, authority);
    }

    /**
     * 检查用户在指定空间是否具有任意一个指定权限
     *
     * @param spaceId      空间ID
     * @param authorities 权限标识数组
     * @return 是否具有任意一个权限
     */
    public boolean hasAnySpaceAuthority(Long spaceId, String... authorities) {
        if (spaceId == null || authorities == null || authorities.length == 0) {
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Long userId = extractUserId(authentication.getPrincipal());
        if (userId == null) {
            return false;
        }

        return spaceRoleService.hasAnyAuthority(userId, spaceId, authorities);
    }

    /**
     * 从 Principal 中提取用户ID
     *
     * @param principal Spring Security Principal
     * @return 用户ID
     */
    private Long extractUserId(Object principal) {
        if (principal instanceof com.wfh.drawio.model.entity.User user) {
            return user.getId();
        } else if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            // 如果是默认的 User 实现,从用户名中提取 ID
            try {
                return Long.parseLong(springUser.getUsername());
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (principal instanceof String) {
            // 如果用户名直接是 String
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
