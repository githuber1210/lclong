package com.example.framework.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.service.INoticeService;
import com.example.models.entity.Notice;
import com.example.models.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Resource
    private NoticeMapper noticeMapper;
    @Override
    public List<Notice> list(String start, String end) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("create_time", start).le("create_time", end);
        return noticeMapper.selectList(queryWrapper);
    }

    @Override
    public Page<Notice> list(String title, Integer pageNum, Integer pageSize) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        Page<Notice> page = noticeMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        return page;
    }
}
