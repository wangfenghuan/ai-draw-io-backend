package com.wfh.drawio.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfh.drawio.common.BaseResponse;
import com.wfh.drawio.common.DeleteRequest;
import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.common.ResultUtils;
import com.wfh.drawio.exception.BusinessException;
import com.wfh.drawio.exception.ThrowUtils;
import com.wfh.drawio.model.dto.announcement.AnnouncementAddRequest;
import com.wfh.drawio.model.dto.announcement.AnnouncementQueryRequest;
import com.wfh.drawio.model.dto.announcement.AnnouncementUpdateRequest;
import com.wfh.drawio.model.entity.Announcement;
import com.wfh.drawio.model.vo.AnnouncementVO;
import com.wfh.drawio.service.AnnouncementService;
import com.wfh.drawio.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: AnnouncementController
 * @Author wangfenghuan
 * @Package com.wfh.drawio.controller
 * @Date 2026/1/24 17:04
 * @description: 公告管理接口
 */
@RestController
@RequestMapping("/announcement")
@Slf4j
public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;

    @Resource
    private UserService userService;

    /**
     * 创建公告
     *
     * @param announcementAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @Operation(summary = "创建公告")
    @PreAuthorize("hasAuthority('admin')")
    public BaseResponse<Long> addAnnouncement(@RequestBody AnnouncementAddRequest announcementAddRequest,
                                              HttpServletRequest request) {
        if (announcementAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(announcementAddRequest, announcement);

        // 从请求中获取当前登录用户ID
        com.wfh.drawio.model.entity.User loginUser = userService.getLoginUser(request);
        announcement.setUserId(loginUser.getId());

        boolean result = announcementService.save(announcement);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(announcement.getId());
    }

    /**
     * 删除公告
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @Operation(summary = "删除公告")
    @PreAuthorize("hasAuthority('admin')")
    public BaseResponse<Boolean> deleteAnnouncement(@RequestBody DeleteRequest deleteRequest,
                                                    HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = announcementService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新公告
     *
     * @param announcementUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @Operation(summary = "更新公告")
    @PreAuthorize("hasAuthority('admin')")
    public BaseResponse<Boolean> updateAnnouncement(@RequestBody AnnouncementUpdateRequest announcementUpdateRequest,
                                                    HttpServletRequest request) {
        if (announcementUpdateRequest == null || announcementUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(announcementUpdateRequest, announcement);
        boolean result = announcementService.updateById(announcement);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取公告
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @Operation(summary = "根据 id 获取公告")
    public BaseResponse<Announcement> getAnnouncementById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Announcement announcement = announcementService.getById(id);
        ThrowUtils.throwIf(announcement == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(announcement);
    }

    /**
     * 根据 id 获取公告封装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    @Operation(summary = "根据 id 获取公告封装类")
    public BaseResponse<AnnouncementVO> getAnnouncementVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Announcement announcement = announcementService.getById(id);
        ThrowUtils.throwIf(announcement == null, ErrorCode.NOT_FOUND_ERROR);
        AnnouncementVO announcementVO = announcementService.getAnnouncementVO(announcement);
        return ResultUtils.success(announcementVO);
    }

    /**
     * 分页获取公告列表
     *
     * @param announcementQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @Operation(summary = "分页获取公告列表")
    public BaseResponse<Page<Announcement>> listAnnouncementByPage(@RequestBody AnnouncementQueryRequest announcementQueryRequest,
                                                                    HttpServletRequest request) {
        if (announcementQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = announcementQueryRequest.getCurrent();
        long size = announcementQueryRequest.getPageSize();
        Page<Announcement> announcementPage = announcementService.page(new Page<>(current, size),
                announcementService.getQueryWrapper(announcementQueryRequest));
        return ResultUtils.success(announcementPage);
    }

    /**
     * 分页获取公告封装列表
     *
     * @param announcementQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    @Operation(summary = "分页获取公告封装列表")
    public BaseResponse<Page<AnnouncementVO>> listAnnouncementVOByPage(@RequestBody AnnouncementQueryRequest announcementQueryRequest,
                                                                        HttpServletRequest request) {
        if (announcementQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = announcementQueryRequest.getCurrent();
        long size = announcementQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Announcement> announcementPage = announcementService.page(new Page<>(current, size),
                announcementService.getQueryWrapper(announcementQueryRequest));
        Page<AnnouncementVO> announcementVOPage = new Page<>(current, size, announcementPage.getTotal());
        List<AnnouncementVO> announcementVOList = announcementService.getAnnouncementVO(announcementPage.getRecords());
        announcementVOPage.setRecords(announcementVOList);
        return ResultUtils.success(announcementVOPage);
    }
}

