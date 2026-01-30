package com.wfh.drawio.security;

import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.service.OAuth2UserSyncService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * OAuth2 用户详情服务
 * 用于在OAuth2认证过程中加载用户信息
 *
 * @author fenghuanwang
 */
@Service
@Slf4j
public class OAuth2UserDetailsService {

    @Resource
    private OAuth2UserSyncService oAuth2UserSyncService;

    /**
     * 加载或创建OAuth2用户
     *
     * @param oAuth2User OAuth2用户信息
     * @return 本地用户实体
     */
    public User loadOAuth2User(OAuth2User oAuth2User) {
        // 假设provider是github，可以根据需要扩展
        return oAuth2UserSyncService.syncOrCreateUser(oAuth2User, "github");
    }
}
