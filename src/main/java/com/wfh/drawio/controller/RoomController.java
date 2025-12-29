package com.wfh.drawio.controller;

import com.wfh.drawio.common.BaseResponse;
import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.common.ResultUtils;
import com.wfh.drawio.exception.ThrowUtils;
import com.wfh.drawio.model.dto.room.RoomAddRequest;
import com.wfh.drawio.model.entity.DiagramRoom;
import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.service.DiagramRoomService;
import com.wfh.drawio.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: RoomController
 * @Author wangfenghuan
 * @Package com.wfh.drawio.controller
 * @Date 2025/12/28 11:05
 * @description:
 */
@RestController
@RequestMapping("/room")
public class RoomController {


    @Resource
    private UserService userService;

    @Resource
    private DiagramRoomService roomService;

    /**
     * 创建房间
     * @param roomAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addRoom(@RequestBody RoomAddRequest roomAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(roomAddRequest == null, ErrorCode.PARAMS_ERROR);

        DiagramRoom room = new DiagramRoom();
        BeanUtils.copyProperties(roomAddRequest, room);
        User loginUser = userService.getLoginUser(request);
        room.setOwerId(loginUser.getId());
        // 写入数据库
        boolean result = roomService.save(room);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newRoomId = room.getId();
        return ResultUtils.success(newRoomId);
    }

}
