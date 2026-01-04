package com.wfh.drawio.controller;

import com.wfh.drawio.mapper.CooperationRoomMapper;
import com.wfh.drawio.model.entity.CooperationRoom;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author fenghuanwang
 */
@RestController
@RequestMapping("/excalidraw")
public class ExcalidrawController {

    @Resource
    private CooperationRoomMapper roomMapper;

    @PostMapping("/{roomId}/save")
    public void save(@PathVariable Long roomId, @RequestBody byte[] encryptedData) {
        CooperationRoom room = new CooperationRoom();
        room.setId( roomId);
        room.setEncryptedData(encryptedData);
        
        // 简单的覆盖保存
        if (roomMapper.updateById(room) == 0) {
            roomMapper.insert(room);
        }
    }
}