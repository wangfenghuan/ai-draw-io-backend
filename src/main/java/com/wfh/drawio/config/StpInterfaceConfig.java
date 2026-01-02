package com.wfh.drawio.config;

import cn.dev33.satoken.model.wrapperInfo.SaDisableWrapperInfo;
import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Title: StpInterfaceConfig
 * @Author wangfenghuan
 * @Package com.wfh.drawio.config
 * @Date 2025/12/29 13:22
 * @description:
 */
@Component
public class StpInterfaceConfig implements StpInterface {
    @Override
    public List<String> getPermissionList(Object o, String s) {
        return List.of();
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        return List.of();
    }

    @Override
    public SaDisableWrapperInfo isDisabled(Object loginId, String service) {
        return StpInterface.super.isDisabled(loginId, service);
    }
}
