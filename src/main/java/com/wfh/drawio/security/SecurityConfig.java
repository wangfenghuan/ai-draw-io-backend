package com.wfh.drawio.security;

import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.exception.BusinessException;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * @Title: SecurityConfig
 * @Author wangfenghuan
 * @Package com.wfh.drawio.security
 * @Date 2026/1/9 13:44
 * @description:
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig{

    @Resource
    private UserDetailsServiceImpl userDetailsService;


    @Resource
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth
                // 注册和登录接口
                .requestMatchers(
                        "/user/register",
                        "/user/login",
                        "/user/logout"
                ).permitAll()
                // 接口文档
                .requestMatchers(
                        "/doc.html",        // Knife4j 接口文档入口
                        "/swagger-ui/**",   // Swagger UI 页面
                        "/swagger-ui.html", // Swagger UI 老版入口
                        "/v3/api-docs/**",  // OpenAPI 3.0 描述数据 (JSON)
                        "/webjars/**"       // Swagger 依赖的静态资源 (JS/CSS)
                ).permitAll().requestMatchers("/excalidraw/**").permitAll()
                // 静态资源
                .requestMatchers("/static/**", "/public/**").permitAll()
                // WebSocket 路径
                .requestMatchers("/excalidraw/**").permitAll()
                // 处理跨域请求的预检请求 (OPTIONS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 其他所有请求需要认证
                .anyRequest().authenticated()
        )
                .exceptionHandling(exceptions -> exceptions
                        // 当用户未登录访问受保护资源时，不要重定向，而是返回 JSON 401
                        .authenticationEntryPoint((request, response, authException) -> {
                            resolver.resolveException(request, response, null, new BusinessException(ErrorCode.NOT_LOGIN_ERROR));
                        })
                        // 当用户权限不足时返回 JSON 403
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            resolver.resolveException(request, response, null, new BusinessException(ErrorCode.NO_AUTH_ERROR));
                        })
                )
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .rememberMe(remember -> remember
                        .userDetailsService(userDetailsService)
                        .tokenValiditySeconds(60 * 60 * 24 * 7) // 7天有效
                )
                .userDetailsService(userDetailsService);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
