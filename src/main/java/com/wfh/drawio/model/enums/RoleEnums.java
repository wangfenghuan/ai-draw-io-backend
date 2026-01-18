package com.wfh.drawio.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RBAC角色枚举
 *
 * @author wangfenghuan
 */
@Getter
public enum RoleEnums {

    DIAGRAM_ADMIN("图表协作房间管理员", "diagram_admin"),
    DIAGRAM_EDITOR("图表协作房间编辑者", "diagram_editor"),
    DIAGRAM_VIEWER("图表协作房间查看者", "diagram_viewer"),
    ALL_ADMIN("超级管理员", "all_admin"),
    SPACE_EDITOR("团队空间编辑", "space_editor"),
    SPACE_VIEWER("团队空间查看", "sapce_viewer"),
    SPACE_ADMIN("团队空间管理员", "sapce_admin");

    private final String text;
    private final String value;

    RoleEnums(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的 value
     * @return 枚举值
     */
    public static RoleEnums getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (RoleEnums anEnum : RoleEnums.values()) {
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
        return Arrays.stream(RoleEnums.values())
                .map(RoleEnums::getText)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有枚举的值列表
     *
     * @return 值列表
     */
    public static List<String> getAllValues() {
        return Arrays.stream(RoleEnums.values())
                .map(RoleEnums::getValue)
                .collect(Collectors.toList());
    }
}
