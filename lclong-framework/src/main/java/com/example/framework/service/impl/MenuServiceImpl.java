package com.example.framework.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.service.IMenuService;
import com.example.models.entity.Menu;
import com.example.models.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public void saveMenu(Menu menu) {
        menu.setCreateTime(DateUtil.now());
        //menu.setCreateBy(TokenUtils.getCurrentUser().getUsername());
        menuMapper.insert(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        menu.setUpdateTime(DateUtil.now());
        //menu.setUpdateBy(TokenUtils.getCurrentUser().getUsername());
        menuMapper.updateById(menu);
    }

    @Override
    public List<Menu> listAllMenus() {
        List<Menu> menuList = list();
        List<Menu> collect = list().stream()
                .filter(menu -> menu.getPid() == null)
                .collect(Collectors.toList());
        for (Menu menu : collect) {
            menu.setChildren(menuList.stream().filter(menu1 -> menu.getId().equals(menu1.getPid())).collect(Collectors.toList()));
        }
        return collect;
    }

}
