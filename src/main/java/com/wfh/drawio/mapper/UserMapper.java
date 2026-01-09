package com.wfh.drawio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wfh.drawio.model.entity.User;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;


/**
 * 用户数据库操作
 *
 * @author wangfenghuan
 * @from wangfenghuan
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from sys_user where userAccount = #{username}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            // 这里的 column = "id" 指的是 ss_user 表的主键 id
            // 它会被作为参数传给 AuthorityMapper.findByUserId(Long userId)
            @Result(property = "authorities", column = "id", javaType = List.class,
                    many = @Many(select = "com.wfh.drawio.mapper.SysAuthorityMapper.findByUserId"))
    })
    Optional<User> findByUsername(String username);

}




