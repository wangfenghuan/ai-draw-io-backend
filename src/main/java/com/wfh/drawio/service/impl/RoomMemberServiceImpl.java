package com.wfh.drawio.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.exception.BusinessException;
import com.wfh.drawio.exception.ThrowUtils;
import com.wfh.drawio.mapper.RoomMemberMapper;
import com.wfh.drawio.model.dto.roommember.RoomMemberAddRequest;
import com.wfh.drawio.model.dto.roommember.RoomMemberQueryRequest;
import com.wfh.drawio.model.entity.DiagramRoom;
import com.wfh.drawio.model.entity.RoomMember;
import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.model.enums.RoleEnums;
import com.wfh.drawio.model.vo.RoomMemberVO;
import com.wfh.drawio.model.vo.UserVO;
import com.wfh.drawio.service.DiagramRoomService;
import com.wfh.drawio.service.RoomMemberService;
import com.wfh.drawio.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author fenghuanwang
* @description 针对表【room_member】的数据库操作Service实现
* @createDate 2025-12-28 11:00:22
*/
@Service
public class RoomMemberServiceImpl extends ServiceImpl<RoomMemberMapper, RoomMember>
    implements RoomMemberService{

    @Resource
    private UserService userService;

    @Resource
    private DiagramRoomService diagramRoomService;

    @Override
    public long addRoomMember(RoomMemberAddRequest roomMemberAddRequest) {
        // 参数校验
        ThrowUtils.throwIf(roomMemberAddRequest == null, ErrorCode.PARAMS_ERROR);
        RoomMember roomMember = new RoomMember();
        BeanUtils.copyProperties(roomMemberAddRequest, roomMember);
        validRoomMember(roomMember, true);
        boolean res = this.save(roomMember);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return roomMember.getId();
    }

    @Override
    public void validRoomMember(RoomMember roomMember, boolean add) {
        ThrowUtils.throwIf(roomMember == null, ErrorCode.PARAMS_ERROR);
        // 创建时，房间 id 和用户 id 必填
        Long roomId = roomMember.getRoomId();
        Long userId = roomMember.getUserId();
        if (add) {
            ThrowUtils.throwIf(ObjUtil.hasEmpty(roomId, userId), ErrorCode.PARAMS_ERROR);
            User user = userService.getById(userId);
            ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
            DiagramRoom room = diagramRoomService.getById(roomId);
            ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR, "房间不存在");
        }
        // 校验房间角色
        String roomRole = roomMember.getRoomRole();
        RoleEnums roleEnum = RoleEnums.getEnumByValue(roomRole);
        if (roomRole != null && roleEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "房间角色不存在");
        }
    }

    @Override
    public QueryWrapper<RoomMember> getQueryWrapper(RoomMemberQueryRequest roomMemberQueryRequest) {
        QueryWrapper<RoomMember> queryWrapper = new QueryWrapper<>();
        if (roomMemberQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = roomMemberQueryRequest.getId();
        Long roomId = roomMemberQueryRequest.getRoomId();
        Long userId = roomMemberQueryRequest.getUserId();
        String roomRole = roomMemberQueryRequest.getRoomRole();
        queryWrapper.eq(ObjUtil.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjUtil.isNotEmpty(roomId), "roomId", roomId);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjUtil.isNotEmpty(roomRole), "roomRole", roomRole);
        return queryWrapper;
    }

    @Override
    public RoomMemberVO getRoomMemberVO(RoomMember roomMember, HttpServletRequest request) {
        // 对象转封装类
        RoomMemberVO roomMemberVO = new RoomMemberVO();
        BeanUtils.copyProperties(roomMember, roomMemberVO);
        // 关联查询用户信息
        Long userId = roomMember.getUserId();
        if (userId != null && userId > 0) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            roomMemberVO.setUserName(userVO.getUserName());
            roomMemberVO.setUserAvatar(userVO.getUserAvatar());
        }
        return roomMemberVO;
    }

    @Override
    public List<RoomMemberVO> getRoomMemberVOList(List<RoomMember> roomMemberList) {
        // 判断输入列表是否为空
        if (CollUtil.isEmpty(roomMemberList)) {
            return Collections.emptyList();
        }
        // 对象列表 => 封装对象列表
        List<RoomMemberVO> roomMemberVOList = roomMemberList.stream()
                .map(roomMember -> {
                    RoomMemberVO vo = new RoomMemberVO();
                    BeanUtils.copyProperties(roomMember, vo);
                    return vo;
                })
                .collect(Collectors.toList());
        // 1. 收集需要关联查询的用户 ID
        Set<Long> userIdSet = roomMemberList.stream()
                .map(RoomMember::getUserId)
                .collect(Collectors.toSet());
        // 2. 批量查询用户
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 3. 填充 RoomMemberVO 的用户信息
        roomMemberVOList.forEach(roomMemberVO -> {
            Long userId = roomMemberVO.getUserId();
            // 填充用户信息
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            UserVO userVO = userService.getUserVO(user);
            roomMemberVO.setUserName(userVO.getUserName());
            roomMemberVO.setUserAvatar(userVO.getUserAvatar());
        });
        return roomMemberVOList;
    }
}



