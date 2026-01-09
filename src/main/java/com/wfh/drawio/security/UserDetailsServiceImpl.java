package com.wfh.drawio.security;

import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.exception.BusinessException;
import com.wfh.drawio.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @Title: UserDetailsServiceImpl
 * @Author wangfenghuan
 * @Package com.wfh.drawio.security
 * @Date 2026/1/9 13:36
 * @description:
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userMapper.findByUsername(username).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在"));
    }
}
