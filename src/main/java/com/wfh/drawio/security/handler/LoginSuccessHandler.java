package com.wfh.drawio.security.handler;

import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.service.OAuth2UserSyncService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 登录成功处理器
 * 处理GitHub等OAuth2登录成功后的逻辑
 *
 * @author fenghuanwang
 */
@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private SecurityContextRepository securityContextRepository;

    @Value("${spring.security.oauth2.client.provider.github.user-name-attribute}")
    private String loginNameAttribute;

    @Resource
    @Lazy
    private OAuth2UserSyncService oauth2UserSyncService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        Object principal = authentication.getPrincipal();
        log.info("OAuth2登录成功，原始Principal: {}", principal);

        if (!(principal instanceof OAuth2User oAuth2User)) {
            log.warn("非OAuth2登录类型，跳过处理");
            return;
        }

        try {
            // 1. 同步或创建本地用户
            User localUser = oauth2UserSyncService.syncOrCreateUser(oAuth2User, "github");
            log.info("GitHub用户登录成功，已同步到本地数据库: userId={}, userAccount={}",
                    localUser.getId(), localUser.getUserAccount());

            // 2. 创建新的认证信息，使用本地用户对象
            // 这一步至关重要，它将认证主体从 OAuth2User 替换为我们自己的 UserDetails 实现
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    localUser, null, localUser.getAuthorities());

            // 3. 创建新的安全上下文并保存
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(newAuth);
            securityContextRepository.saveContext(context, request, response);

            // 4. 重定向到前端页面
            String redirectUrl = determineRedirectUrl(request);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("处理OAuth2登录失败", e);
            response.sendRedirect("/login?error=oauth2_failed");
        }
    }

    /**
     * 确定登录成功后的重定向URL
     */
    private String determineRedirectUrl(HttpServletRequest request) {
        // 重定向到配置的前端地址
        return frontendUrl;
    }
}

