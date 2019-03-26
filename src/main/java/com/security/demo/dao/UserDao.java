package com.security.demo.dao;

import com.security.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @author fanglingxiao
 * @createTime 2019/3/26
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
    /**
     * 使用用户名查询用户信息
     * @param userName username
     * @return User
     */
    User selectUserByName(String userName);
}
