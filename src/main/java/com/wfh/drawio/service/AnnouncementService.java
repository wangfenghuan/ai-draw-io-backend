package com.wfh.drawio.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wfh.drawio.model.dto.announcement.AnnouncementQueryRequest;
import com.wfh.drawio.model.entity.Announcement;
import com.wfh.drawio.model.vo.AnnouncementVO;

import java.util.List;

/**
* @author fenghuanwang
* @description 针对表【announcement(Announcement 公告表)】的数据库操作Service
* @createDate 2026-01-24 17:02:45
*/
public interface AnnouncementService extends IService<Announcement> {

    /**
     * 获取查询条件
     *
     * @param announcementQueryRequest
     * @return
     */
    QueryWrapper<Announcement> getQueryWrapper(AnnouncementQueryRequest announcementQueryRequest);

    /**
     * 获取公告VO封装
     *
     * @param announcement
     * @return
     */
    AnnouncementVO getAnnouncementVO(Announcement announcement);

    /**
     * 获取公告VO封装列表
     *
     * @param announcementList
     * @return
     */
    List<AnnouncementVO> getAnnouncementVO(List<Announcement> announcementList);
}
