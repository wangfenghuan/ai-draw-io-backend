package com.wfh.drawio.model.vo;

import cn.hutool.json.JSONUtil;
import com.wfh.drawio.model.entity.Diagram;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 图表视图
 *
 * @author fenghuanwang
 */
@Data
public class DiagramVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String name;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;



    /**
     * 封装类转对象
     *
     * @param diagramVO
     * @return
     */
    public static Diagram voToObj(DiagramVO diagramVO) {
        if (diagramVO == null) {
            return null;
        }
        Diagram diagram = new Diagram();
        BeanUtils.copyProperties(diagramVO, diagram);
        return diagram;
    }

    /**
     * 对象转封装类
     *
     * @param diagram
     * @return
     */
    public static DiagramVO objToVo(Diagram diagram) {
        if (diagram == null) {
            return null;
        }
        DiagramVO diagramVO = new DiagramVO();
        BeanUtils.copyProperties(diagram, diagramVO);
        return diagramVO;
    }
}
