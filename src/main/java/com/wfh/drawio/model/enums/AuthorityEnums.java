package com.wfh.drawio.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RBAC权限枚举
 *
 * @author wangfenghuan
 */
@Getter
public enum AuthorityEnums {

    ROOM_DIAGRAM_EDIT("编辑房间图表", "room:diagram:edit"),
    ROOM_DIAGRAM_VIEW("查看房间图表", "room:diagram:view"),
    ROOM_USER_MANAGE("房间用户管理", "room:user:manage"),
    ADMIN("超级管理员", "admin"),
    SPACE_USER_MANAGE("团队空间用户管理", "space:user:manage"),
    SPACE_DIAGRAM_ADD("团队空间图表创建", "space:diagram:add"),
    SPACE_DIAGRAM_EDIT("团队空间编辑图表", "space:diagram:edit"),
    SPACE_DIAGRAM_DELETE("团队空间删除图表", "space:diagram:delete"),
    SPACE_DIAGRAM_VIEW("团队空间查看图表", "space:diagram:view");

    private final String text;
    private final String value;

    AuthorityEnums(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的 value
     * @return 枚举值
     */
    public static AuthorityEnums getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (AuthorityEnums anEnum : AuthorityEnums.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 获取所有枚举的文本列表
     *
     * @return 文本列表
     */
    public static List<String> getAllTexts() {
        return Arrays.stream(AuthorityEnums.values())
                .map(AuthorityEnums::getText)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有枚举的值列表
     *
     * @return 值列表
     */
    public static List<String> getAllValues() {
        return Arrays.stream(AuthorityEnums.values())
                .map(AuthorityEnums::getValue)
                .collect(Collectors.toList());
    }
}
