package com.kaede.mapper;

import com.kaede.pojo.Role;
import com.kaede.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author kaede
 * @create 2022-09-21
 */

@Mapper
public interface UserMapper {

    List<Role> getUserRoleByUid(Integer uid);

    User loadUserByUsername(String username);

}
