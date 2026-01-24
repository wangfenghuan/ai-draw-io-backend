package com.wfh.drawio.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.exception.BusinessException;
import com.wfh.drawio.mapper.MaterialMapper;
import com.wfh.drawio.model.dto.material.MaterialQueryRequest;
import com.wfh.drawio.model.entity.Material;
import com.wfh.drawio.model.vo.MaterialVO;
import com.wfh.drawio.service.MaterialService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author fenghuanwang
* @description 针对表【material(素材表)】的数据库操作Service实现
* @createDate 2026-01-24 14:39:56
*/
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material>
    implements MaterialService{

    @Override
    public QueryWrapper<Material> getQueryWrapper(MaterialQueryRequest materialQueryRequest) {
        if (materialQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = materialQueryRequest.getId();
        String name = materialQueryRequest.getName();
        String tags = materialQueryRequest.getTags();
        Long userId = materialQueryRequest.getUserId();
        String sortField = materialQueryRequest.getSortField();
        String sortOrder = materialQueryRequest.getSortOrder();

        QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(userId != null, "user_id", userId);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(tags), "tags", tags);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                "desc".equals(sortOrder), sortField);
        return queryWrapper;
    }

    @Override
    public MaterialVO getMaterialVO(Material material) {
        if (material == null) {
            return null;
        }
        MaterialVO materialVO = MaterialVO.objToVo(material);
        return materialVO;
    }

    @Override
    public List<MaterialVO> getMaterialVO(List<Material> materialList) {
        if (CollUtil.isEmpty(materialList)) {
            return List.of();
        }
        return materialList.stream().map(this::getMaterialVO).collect(Collectors.toList());
    }
}




