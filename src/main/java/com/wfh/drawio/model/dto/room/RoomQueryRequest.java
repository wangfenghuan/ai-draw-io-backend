package com.wfh.drawio.model.dto.room;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.wfh.drawio.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: RoomQueryRequest
 * @Author wangfenghuan
 * @Package com.wfh.drawio.model.dto.room
 * @Date 2025/12/29 09:46
 * @description: 房间查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "RoomQueryRequest", description = "房间查询请求")
public class RoomQueryRequest extends PageRequest implements Serializable {

    /**
     * 房间id
     */
    @Schema(description = "房间ID", example = "123456789")
    private Long id;

    /**
     * 房间名称
     */
    @Schema(description = "房间名称", example = "协作编辑房间1")
    private String roomName;

    /**
     * 搜索词
     */
    @Schema(description = "搜索关键词", example = "架构")
    private String searchText;

    /**
     * 房间所关联的图表id
     */
    @Schema(description = "图表ID", example = "20001")
    private Long diagramId;

    /**
     * 创建者id
     */
    @Schema(description = "创建者ID", example = "10001")
    private Long owerId;

    /**
     * 0代表公开，1代表不公开
     */
    @Schema(description = "是否公开（0公开，1私有）", example = "0")
    private Integer isPublic;


    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2024-01-01 10:00:00")
    private Date updateTime;

    /**
     * 是否关闭
     */
    @Schema(description = "是否关闭（0开启，1关闭）", example = "0")
    private Integer isOpen;

    /**
     * 空间id
     */
    @Schema(description = "空间ID", example = "1111111")
    private Long spaceId;

    /**
     * 是否查询空空间id的记录
     */
    @Schema(description = "是否查询空空间id的记录", example = "false")
    private Boolean nullSpaceId;

}
