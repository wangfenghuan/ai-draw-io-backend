package com.wfh.drawio.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfh.drawio.common.BaseResponse;
import com.wfh.drawio.common.ResultUtils;
import com.wfh.drawio.model.entity.Conversion;
import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.service.ConversionService;
import com.wfh.drawio.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @Title: ConversionController
 * @Author wangfenghuan
 * @Package com.wfh.drawio.controller
 * @Date 2025/12/23 16:48
 * @description:
 */
@RestController
@RequestMapping("/conversion")
public class ConversionController {


    @Resource
    private UserService userService;

    @Resource
    private ConversionService conversionService;

    /**
     * 分页查询某个图表的对话历史
     * @param diagramId
     * @param pageSize
     * @param lasteCreateTime
     * @param request
     * @return
     */
    @GetMapping("/diagram/{diagramId}")
    @Operation(summary = "分页查询某个图表的对话历史")
    public BaseResponse<Page<Conversion>> listDiagramChatHistory(
            @PathVariable Long diagramId,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false)LocalDateTime lasteCreateTime,
            HttpServletRequest request
            ){
        User loginUser = userService.getLoginUser(request);
        Page<Conversion> conversionPage = conversionService.listDiagramChatHistoryByPage(diagramId, pageSize, lasteCreateTime, loginUser);
        return ResultUtils.success(conversionPage);
    }

}
