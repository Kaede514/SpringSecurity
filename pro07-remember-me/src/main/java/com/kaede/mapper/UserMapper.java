package com.kaede.mapper;

import com.kaede.pojo.Role;
import com.kaede.pojo.User;
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
    User loadUserByUsername(@Param("username") String username);

    //根据用户id查询用户角色信息
    List<Role> getRolesByUid(@Param("uid") Integer uid);

    //更改密码
    Integer updatePassword(@Param("username") String username, @Param("password") String password);

}
