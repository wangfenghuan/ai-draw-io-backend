package com.wfh.drawio.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.exception.BusinessException;
import com.wfh.drawio.mapper.AnnouncementMapper;
import com.wfh.drawio.model.dto.announcement.AnnouncementQueryRequest;
import com.wfh.drawio.model.entity.Announcement;
import com.wfh.drawio.model.vo.AnnouncementVO;
import com.wfh.drawio.service.AnnouncementService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author fenghuanwang
* @description 针对表【announcement(Announcement 公告表)】的数据库操作Service实现
* @createDate 2026-01-24 17:02:45
*/
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement>
    implements AnnouncementService{

    @Override
    public QueryWrapper<Announcement> getQueryWrapper(AnnouncementQueryRequest announcementQueryRequest) {
        if (announcementQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = announcementQueryRequest.getId();
        String title = announcementQueryRequest.getTitle();
        Long userId = announcementQueryRequest.getUserId();
        Integer priority = announcementQueryRequest.getPriority();
        String sortField = announcementQueryRequest.getSortField();
        String sortOrder = announcementQueryRequest.getSortOrder();

        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(userId != null, "user_id", userId);
        queryWrapper.eq(priority != null, "priority", priority);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                "desc".equals(sortOrder), sortField);
        return queryWrapper;
    }

    @Override
    public AnnouncementVO getAnnouncementVO(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        AnnouncementVO announcementVO = AnnouncementVO.objToVo(announcement);
        return announcementVO;
    }

    @Override
    public List<AnnouncementVO> getAnnouncementVO(List<Announcement> announcementList) {
        if (CollUtil.isEmpty(announcementList)) {
            return List.of();
        }
        return announcementList.stream().map(this::getAnnouncementVO).collect(Collectors.toList());
    }
}




