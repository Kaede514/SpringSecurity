package com.kaede.security.metasource;

import com.kaede.pojo.Menu;
import com.kaede.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * @author kaede
 * @create 2022-09-21
 */

@Component
public class CustomSecurityMetaSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuService menuService;

    //用来做路径对比
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    //自定义动态资源权限的元数据信息
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //当前的请求对象
        String requestURI = ((FilterInvocation) o).getRequest().getRequestURI();
        //查询所有菜单
        List<Menu> allMenu = menuService.getAllMenu();
        for (Menu menu : allMenu) {
            if (antPathMatcher.match(menu.getPattern(), requestURI)) {
                String[] roles = menu.getRoles().stream().map(r -> r.getName()).toArray(String[]::new);
                //返回当前路径所需的角色
                return SecurityConfig.createList(roles);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
