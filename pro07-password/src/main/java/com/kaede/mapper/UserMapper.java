package com.kaede.mapper;

import com.kaede.entity.Role;
import com.kaede.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kaede
 * @create 2022-09-18
 */

@Mapper
public interface UserMapper {

    //根据用户名返回用户
    User loadUserByUsername(String username);

    //根据用户id查询用户角色信息
    List<Role> getRolesByUid(Integer uid);

    //根据用户名更新密码
    Integer updatePassword(@Param("username") String username, @Param("password") String password);

}
