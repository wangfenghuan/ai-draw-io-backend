package com.wfh.drawio.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: RoomVO
 * @Author wangfenghuan
 * @Package com.wfh.drawio.model.vo
 * @Date 2025/12/28 11:21
 * @description:
 */
public class RoomVO implements Serializable {

    /**
     * 房间id
     */
    private Long id;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 房间所关联的图表id
     */
    private Long diagramId;

    /**
     * 创建者id
     */
    private Long owerId;

    /**
     * 0代表公开，1代表不公开
     */
    private Integer isPublic;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否关闭
     */
    private Integer isOpen;


}
