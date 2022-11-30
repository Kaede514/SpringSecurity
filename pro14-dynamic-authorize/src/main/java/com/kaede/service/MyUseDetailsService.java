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
 * @create 2022-09-21
 */

@Service
public class MyUseDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Autowired
    public MyUseDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(s);
        if(ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException("用户名不正确");
        List<Role> roles = userMapper.getUserRoleByUid(user.getId());
        user.setRoles(roles);
        return user;
    }

}
