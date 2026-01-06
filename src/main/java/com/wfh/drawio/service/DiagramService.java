package com.wfh.drawio.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wfh.drawio.model.dto.diagram.DiagramAddRequest;
import com.wfh.drawio.model.dto.diagram.DiagramQueryRequest;
import com.wfh.drawio.model.entity.Diagram;
import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.model.vo.DiagramVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * 图表服务
 *
 * @author fenghuanwang
 */
public interface DiagramService extends IService<Diagram> {


    boolean tryAcquireLock(String roomName);

    /**
     * 下载
     * @param remoteUrl
     * @param fileName
     * @param response
     */
    void download(String remoteUrl, String fileName, HttpServletResponse response);

    /**
     * 校验数据
     *
     * @param diagram
     * @param add 对创建的数据进行校验
     */
    void validDiagram(Diagram diagram, boolean add);

    /**
     * 获取查询条件
     *
     * @param diagramQueryRequest
     * @return
     */
    QueryWrapper<Diagram> getQueryWrapper(DiagramQueryRequest diagramQueryRequest);
    
    /**
     * 获取图表封装
     *
     * @param diagram
     * @param request
     * @return
     */
    DiagramVO getDiagramVO(Diagram diagram, HttpServletRequest request);

    /**
     * 分页获取图表封装
     *
     * @param diagramPage
     * @param request
     * @return
     */
    Page<DiagramVO> getDiagramVOPage(Page<Diagram> diagramPage, HttpServletRequest request);

    /**
     * 上传图表文件并更新空间额度（带事务）
     *
     * @param diagramId 图表ID
     * @param spaceId 空间ID
     * @param fileUrl 文件URL
     * @param fileSize 文件大小
     * @param extension 文件扩展名
     * @param loginUser 登录用户
     */
    void uploadDiagramWithQuota(Long diagramId, Long spaceId, String fileUrl, Long fileSize, String extension, User loginUser);

    /**
     * 删除图表并释放额度（带事务）
     *
     * @param id 图表ID
     */
    void deleteDiagramWithQuota(Long id);

    /**
     * 创建图表并更新空间额度（带事务）
     *
     * @param diagramAddRequest
     * @param loginUser
     * @return
     */
    Long addDiagramWithQuota(DiagramAddRequest diagramAddRequest, User loginUser);
}
