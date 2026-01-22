package com.wfh.drawio.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wfh.drawio.model.dto.roommember.RoomMemberAddRequest;
import com.wfh.drawio.model.dto.roommember.RoomMemberQueryRequest;
import com.wfh.drawio.model.entity.RoomMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wfh.drawio.model.vo.RoomMemberVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author fenghuanwang
* @description 针对表【room_member】的数据库操作Service
* @createDate 2025-12-28 11:00:22
*/
public interface RoomMemberService extends IService<RoomMember> {

    /**
     * 添加房间成员
     *
     * @param roomMemberAddRequest 添加房间成员请求
     * @return 房间成员关系ID
     */
    long addRoomMember(RoomMemberAddRequest roomMemberAddRequest);

    /**
     * 校验房间成员数据
     *
     * @param roomMember 房间成员实体
     * @param add 是否为添加操作
     */
    void validRoomMember(RoomMember roomMember, boolean add);

    /**
     * 获取查询条件
     *
     * @param roomMemberQueryRequest 查询请求
     * @return 查询条件
     */
    QueryWrapper<RoomMember> getQueryWrapper(RoomMemberQueryRequest roomMemberQueryRequest);

    /**
     * 获取房间成员视图对象
     *
     * @param roomMember 房间成员实体
     * @param request HTTP请求
     * @return 房间成员视图对象
     */
    RoomMemberVO getRoomMemberVO(RoomMember roomMember, HttpServletRequest request);

    /**
     * 获取房间成员视图对象列表
     *
     * @param roomMemberList 房间成员实体列表
     * @return 房间成员视图对象列表
     */
    List<RoomMemberVO> getRoomMemberVOList(List<RoomMember> roomMemberList);
}
