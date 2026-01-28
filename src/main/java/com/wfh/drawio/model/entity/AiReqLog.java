package com.wfh.drawio.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @author fenghuanwang
 * @TableName ai_req_log
 */
@TableName(value ="ai_req_log")
@Data
public class AiReqLog {
    /**
     * 日志主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 当日请求时间
     */
    private Integer count;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}