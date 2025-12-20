package com.wfh.drawio.model.dto.diagram;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑图表请求
 *
 * @author fenghuanwang
 */
@Data
public class DiagramEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;


    private static final long serialVersionUID = 1L;
}