package com.wfh.drawio.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wfh.drawio.mapper.SysRoleMapper;
import com.wfh.drawio.mapper.SysRoleAuthorityRelMapper;
import com.wfh.drawio.model.entity.SysAuthority;
import com.wfh.drawio.model.entity.SysRole;
import com.wfh.drawio.model.entity.SysRoleAuthorityRel;
import com.wfh.drawio.model.enums.RoleEnums;
import com.wfh.drawio.service.RoomMemberService;
import com.wfh.drawio.service.RoomRoleService;
import com.wfh.drawio.service.SysAuthorityService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 房间角色服务实现类
 *
 * @author wangfenghuan
 */
@Slf4j
@Service
public class RoomRoleServiceImpl implements RoomRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleAuthorityRelMapper sysRoleAuthorityRelMapper;

    @Resource
    private SysAuthorityService sysAuthorityService;

    @Resource
    private RoomMemberService roomMemberService;

    @Override
    public List<SysAuthority> getAuthoritiesByRoomRole(String roomRole) {
        if (ObjUtil.isEmpty(roomRole)) {
            return Collections.emptyList();
        }

        // 将房间角色转换为角色枚举
        RoleEnums roleEnum = RoleEnums.getEnumByValue(roomRole);
        if (roleEnum == null) {
            log.warn("未找到对应的角色枚举: {}", roomRole);
            return Collections.emptyList();
        }

        return getAuthoritiesByRoleEnum(roleEnum);
    }

    @Override
    public List<SysAuthority> getAuthoritiesByRoleEnum(RoleEnums roleEnum) {
        if (roleEnum == null) {
            return Collections.emptyList();
        }

        // 只处理协作房间角色
        if (roleEnum != RoleEnums.DIAGRAM_ADMIN &&
            roleEnum != RoleEnums.DIAGRAM_EDITOR &&
            roleEnum != RoleEnums.DIAGRAM_VIEWER) {
            log.warn("非协作房间角色: {}", roleEnum.getValue());
            return Collections.emptyList();
        }

        // 根据角色名称查询角色
        SysRole sysRole = sysRoleMapper.selectOne(
                new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getName, roleEnum.getValue())
        );

        if (sysRole == null) {
            log.warn("未找到角色: {}", roleEnum.getValue());
            return Collections.emptyList();
        }

        // 查询角色关联的权限
        List<SysRoleAuthorityRel> roleAuthorityRels = sysRoleAuthorityRelMapper.selectList(
                new LambdaQueryWrapper<SysRoleAuthorityRel>()
                        .eq(SysRoleAuthorityRel::getRoleId, sysRole.getId())
        );

        if (roleAuthorityRels.isEmpty()) {
            log.warn("角色 {} 没有配置任何权限", roleEnum.getValue());
            return Collections.emptyList();
        }

        // 获取权限ID列表
        List<Long> authorityIds = roleAuthorityRels.stream()
                .map(SysRoleAuthorityRel::getAuthorityId)
                .collect(Collectors.toList());

        // 根据权限ID列表查询权限详情
        List<SysAuthority> authorities = sysAuthorityService.lambdaQuery()
                .in(SysAuthority::getId, authorityIds)
                .list();

        log.info("角色 {} 拥有 {} 个权限", roleEnum.getValue(), authorities.size());
        return authorities;
    }

    @Override
    public boolean hasAuthority(Long userId, Long roomId, String authority) {
        if (ObjUtil.hasEmpty(userId, roomId, authority)) {
            return false;
        }

        // 查询用户在房间中的角色
        var roomMember = roomMemberService.lambdaQuery()
                .eq(com.wfh.drawio.model.entity.RoomMember::getRoomId, roomId)
                .eq(com.wfh.drawio.model.entity.RoomMember::getUserId, userId)
                .one();

        if (roomMember == null) {
            return false;
        }

        // 获取角色对应的权限列表
        List<SysAuthority> authorities = getAuthoritiesByRoomRole(roomMember.getRoomRole());

        // 检查是否包含指定权限
        return authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals(authority));
    }

    @Override
    public boolean hasAnyAuthority(Long userId, Long roomId, String... authorities) {
        if (ObjUtil.hasEmpty(userId, roomId, authorities)) {
            return false;
        }

        // 查询用户在房间中的角色
        var roomMember = roomMemberService.lambdaQuery()
                .eq(com.wfh.drawio.model.entity.RoomMember::getRoomId, roomId)
                .eq(com.wfh.drawio.model.entity.RoomMember::getUserId, userId)
                .one();

        if (roomMember == null) {
            return false;
        }

        // 获取角色对应的权限列表
        List<SysAuthority> userAuthorities = getAuthoritiesByRoomRole(roomMember.getRoomRole());

        // 检查是否包含任意一个指定权限
        for (String authority : authorities) {
            boolean hasAuth = userAuthorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals(authority));
            if (hasAuth) {
                return true;
            }
        }

        return false;
    }
}
