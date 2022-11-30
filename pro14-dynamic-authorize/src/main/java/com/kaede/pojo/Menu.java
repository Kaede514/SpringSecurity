package com.kaede.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author kaede
 * @create 2022-09-21
 */

@Data
public class Menu {
    private Integer id;
    private String pattern;
    private List<Role> roles;
}
