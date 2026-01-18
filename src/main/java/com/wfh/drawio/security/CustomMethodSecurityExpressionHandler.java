package com.wfh.drawio.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 自定义方法安全表达式处理器
 * 负责创建 SpaceSecurityExpressionRoot 实例
 * 支持团队空间权限和协作房间权限检查
 *
 * @author wangfenghuan
 */
@Primary  // 确保优先使用这个 Bean
@Component
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final SpaceSecurityService spaceSecurityService;
    private final RoomSecurityService roomSecurityService;

    // 推荐使用构造器注入
    public CustomMethodSecurityExpressionHandler(
            SpaceSecurityService spaceSecurityService,
            RoomSecurityService roomSecurityService,
            ApplicationContext applicationContext) {
        this.spaceSecurityService = spaceSecurityService;
        this.roomSecurityService = roomSecurityService;
        // 这一步很重要，将 ApplicationContext 设置给父类，以便它能解析 Bean (例如 @beanName)
        super.setApplicationContext(applicationContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        SpaceSecurityExpressionRoot root = new SpaceSecurityExpressionRoot(authentication);

        // 注入自定义服务
        root.setSpaceSecurityService(spaceSecurityService);
        root.setRoomSecurityService(roomSecurityService);

        // 注入父类的标准配置
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(getTrustResolver());
        root.setRoleHierarchy(getRoleHierarchy());

        // 设置默认的权限检查前缀 (ROLE_)
        root.setDefaultRolePrefix(getDefaultRolePrefix());

        // 设置 'this' 对象，以便在表达式中访问方法所属的对象
        root.setThis(invocation.getThis());

        // 添加日志，确认自定义处理器被调用
        System.out.println("✅ CustomMethodSecurityExpressionHandler.createSecurityExpressionRoot() 被调用");

        return root;
    }
}