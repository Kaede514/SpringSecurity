package com.kaede.mapper;

import com.kaede.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author kaede
 * @create 2022-09-21
 */

@Mapper
public interface MenuMapper {

    List<Menu> getAllMenu();

}

