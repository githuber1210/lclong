package com.example.framework.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.models.entity.Notice;

import java.util.List;


public interface INoticeService extends IService<Notice> {

    List<Notice> list(String start, String end);

    Page<Notice> list(String title, Integer pageNum, Integer pageSize);
}
