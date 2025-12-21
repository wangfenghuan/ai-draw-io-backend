package com.wfh.drawio.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.exception.ThrowUtils;
import com.wfh.drawio.model.dto.diagram.DiagramQueryRequest;
import com.wfh.drawio.model.entity.Diagram;
import com.wfh.drawio.mapper.DiagramMapper;
import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.model.vo.DiagramVO;
import com.wfh.drawio.service.DiagramService;
import com.wfh.drawio.service.UserService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 图表服务实现
 *
 */
@Service
@Slf4j
public class DiagramServiceImpl extends ServiceImpl<DiagramMapper, Diagram> implements DiagramService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param diagram
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validDiagram(Diagram diagram, boolean add) {
        ThrowUtils.throwIf(diagram == null, ErrorCode.PARAMS_ERROR);
        String name = diagram.getName();
        String diagramCode = diagram.getDiagramCode();
        Long userId = diagram.getUserId();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(ObjectUtils.isEmpty(userId), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(name) || StringUtils.isNotBlank(diagramCode)) {
            ThrowUtils.throwIf(name.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param diagramQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Diagram> getQueryWrapper(DiagramQueryRequest diagramQueryRequest) {
        QueryWrapper<Diagram> queryWrapper = new QueryWrapper<>();
        if (diagramQueryRequest == null) {
            return queryWrapper;
        }
        Long id = diagramQueryRequest.getId();
        String name = diagramQueryRequest.getTitle();
        String searchText = diagramQueryRequest.getSearchText();
        Long userId = diagramQueryRequest.getUserId();
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("name", searchText).or().like("name", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        return queryWrapper;
    }

    /**
     * 获取图表封装
     *
     * @param diagram
     * @param request
     * @return
     */
    @Override
    public DiagramVO getDiagramVO(Diagram diagram, HttpServletRequest request) {
        // 对象转封装类
        return DiagramVO.objToVo(diagram);
    }

    /**
     * 分页获取图表封装
     *
     * @param diagramPage
     * @param request
     * @return
     */
    @Override
    public Page<DiagramVO> getDiagramVOPage(Page<Diagram> diagramPage, HttpServletRequest request) {
        List<Diagram> diagramList = diagramPage.getRecords();
        Page<DiagramVO> diagramVOPage = new Page<>(diagramPage.getCurrent(), diagramPage.getSize(), diagramPage.getTotal());
        if (CollUtil.isEmpty(diagramList)) {
            return diagramVOPage;
        }
        // 对象列表 => 封装对象列表
        List<DiagramVO> diagramVOList = diagramList.stream().map(DiagramVO::objToVo).collect(Collectors.toList());
        diagramVOPage.setRecords(diagramVOList);
        return diagramVOPage;
    }

}
