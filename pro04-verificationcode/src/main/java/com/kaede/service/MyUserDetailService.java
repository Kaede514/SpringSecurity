package com.kaede.service;

import com.kaede.mapper.UserMapper;
import com.kaede.pojo.Role;
import com.kaede.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author kaede
 * @create 2022-09-18
 */

@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserMapper userMapper;

    @Autowired
    public MyUserDetailService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //1.查询用户
        User user = userMapper.loadUserByUsername(s);
        if(ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException("用户名不正确");
        //2.查询权限信息
        List<Role> roles = userMapper.getRolesByUid(user.getId());
        user.setRoles(roles);
        return user;
    }

}
