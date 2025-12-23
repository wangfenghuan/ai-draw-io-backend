package com.wfh.drawio.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfh.drawio.model.entity.Conversion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wfh.drawio.model.entity.User;

import java.time.LocalDateTime;

/**
* @author fenghuanwang
* @description 针对表【conversion(消息对话表)】的数据库操作Service
* @createDate 2025-12-21 09:19:15
*/
public interface ConversionService extends IService<Conversion> {

    /**
     * 分页查询对话记录
     *
     * @param appId
     * @param pageSize
     * @param lastCreateTime
     * @param loginUser
     * @return
     */
    Page<Conversion> listDiagramChatHistoryByPage(Long appId, int pageSize, LocalDateTime lastCreateTime, User loginUser);

}
