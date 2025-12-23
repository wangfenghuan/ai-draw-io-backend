package com.wfh.drawio.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.exception.BusinessException;
import com.wfh.drawio.exception.ThrowUtils;
import com.wfh.drawio.model.dto.conversion.ConversionQueryRequest;
import com.wfh.drawio.model.entity.Conversion;
import com.wfh.drawio.model.entity.Diagram;
import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.service.ConversionService;
import com.wfh.drawio.mapper.ConversionMapper;
import com.wfh.drawio.service.DiagramService;
import com.wfh.drawio.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author fenghuanwang
* @description 针对表【conversion(消息对话表)】的数据库操作Service实现
* @createDate 2025-12-21 09:19:15
*/
@Service
public class ConversionServiceImpl extends ServiceImpl<ConversionMapper, Conversion>
    implements ConversionService{

    @Resource
    private DiagramService diagramService;

    @Resource
    private UserService userService;

    @Override
    public Page<Conversion> listDiagramChatHistoryByPage(Long diagramId, int pageSize, LocalDateTime lastCreateTime, User loginUser) {
        ThrowUtils.throwIf(diagramId == null || diagramId <= 0, ErrorCode.PARAMS_ERROR, "图表ID不能为空");
        ThrowUtils.throwIf(pageSize <= 0 ||pageSize > 10000 , ErrorCode.PARAMS_ERROR, "图表ID不能为空");
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        // 权限校验，只有管理员和创建者才能查看

        Diagram diagram = diagramService.getById(diagramId);
        ThrowUtils.throwIf(diagram == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        if (!diagram.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        ConversionQueryRequest conversionQueryRequest = new ConversionQueryRequest();
        conversionQueryRequest.setDiagramId(diagramId);
        conversionQueryRequest.setLastCreateTime(lastCreateTime);
        conversionQueryRequest.setPageSize(pageSize);
        conversionQueryRequest.setUserId(loginUser.getId());
        QueryWrapper queryWrapper = this.getQueryWrapper(conversionQueryRequest);
        return this.page(Page.of(1, pageSize), queryWrapper);
    }

    /**
     * 获取查询包装器
     * @param conversionQueryRequest
     * @return
     */
    public QueryWrapper getQueryWrapper(ConversionQueryRequest conversionQueryRequest) {
        QueryWrapper<Conversion> queryWrapper = new QueryWrapper<>(Conversion.class);

        if (conversionQueryRequest == null) {
            return queryWrapper;
        }

        String message = conversionQueryRequest.getMessage();
        String messageType = conversionQueryRequest.getMessageType();
        Long diagramId = conversionQueryRequest.getDiagramId();
        Long userId = conversionQueryRequest.getUserId();
        String sortField = conversionQueryRequest.getSortField();
        String sortOrder = conversionQueryRequest.getSortOrder();
        LocalDateTime lastCreateTime = conversionQueryRequest.getLastCreateTime();

        // 拼接查询条件
        queryWrapper.eq("diagramId", diagramId)
                .like(StringUtils.isNotBlank(message),"message", message)
                .eq(StringUtils.isNotBlank(messageType), "messageType", messageType)
                .eq("userId", userId);
        // 游标查询逻辑， 只使用createTime作为游标
        if (lastCreateTime != null){
            queryWrapper.lt("createTime", lastCreateTime);
        }

        if (StrUtil.isNotBlank(sortField)){
            queryWrapper.orderBy(true,"ascend".equals(sortOrder),sortField);
        }else {
            // 默认按照创建时间降序排列
            queryWrapper.orderBy(true, false, "createTime");
        }
        return queryWrapper;
    }
}




