package com.wfh.drawio.model.dto.diagram;

import com.wfh.drawio.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询图表请求
 *
 * @author fenghuanwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DiagramQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;


    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String diagramCode;


    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}