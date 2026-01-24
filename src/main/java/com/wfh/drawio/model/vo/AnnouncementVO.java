package com.wfh.drawio.model.vo;

import com.wfh.drawio.model.entity.Announcement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 公告视图
 *
 * @author fenghuanwang
 */
@Data
@Schema(name = "AnnouncementVO", description = "公告视图对象")
public class AnnouncementVO implements Serializable {

    /**
     * id
     */
    @Schema(description = "公告ID", example = "123456789")
    private Long id;

    /**
     * 公告标题
     */
    @Schema(description = "公告标题", example = "系统维护通知")
    private String title;

    /**
     * 公告内容
     */
    @Schema(description = "公告内容", example = "系统将于今晚22:00进行维护，预计持续2小时")
    private String content;

    /**
     * 优先级（1优先级最高，0代表取消公告）
     */
    @Schema(description = "优先级（1优先级最高，0代表取消公告）", example = "1")
    private Integer priority;

    /**
     * 发布用户 id
     */
    @Schema(description = "发布用户ID", example = "10001")
    private Long userId;

    /**
     * 发布用户信息
     */
    @Schema(description = "发布用户信息")
    private UserVO userVO;

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
     * 封装类转对象
     *
     * @param announcementVO
     * @return
     */
    public static Announcement voToObj(AnnouncementVO announcementVO) {
        if (announcementVO == null) {
            return null;
        }
        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(announcementVO, announcement);
        return announcement;
    }

    /**
     * 对象转封装类
     *
     * @param announcement
     * @return
     */
    public static AnnouncementVO objToVo(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        AnnouncementVO announcementVO = new AnnouncementVO();
        BeanUtils.copyProperties(announcement, announcementVO);
        return announcementVO;
    }
}
