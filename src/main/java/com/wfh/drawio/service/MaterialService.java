package com.wfh.drawio.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wfh.drawio.model.dto.material.MaterialQueryRequest;
import com.wfh.drawio.model.entity.Material;
import com.wfh.drawio.model.vo.MaterialVO;

import java.util.List;

/**
* @author fenghuanwang
* @description 针对表【material(素材表)】的数据库操作Service
* @createDate 2026-01-24 14:39:56
*/
public interface MaterialService extends IService<Material> {

    /**
     * 获取查询条件
     *
     * @param materialQueryRequest
     * @return
     */
    QueryWrapper<Material> getQueryWrapper(MaterialQueryRequest materialQueryRequest);

    /**
     * 获取素材VO封装
     *
     * @param material
     * @return
     */
    MaterialVO getMaterialVO(Material material);

    /**
     * 获取素材VO封装列表
     *
     * @param materialList
     * @return
     */
    List<MaterialVO> getMaterialVO(List<Material> materialList);
}
