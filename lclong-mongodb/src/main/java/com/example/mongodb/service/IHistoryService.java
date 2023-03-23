package com.example.mongodb.service;


import com.example.mongodb.domain.History;

import java.util.List;

public interface IHistoryService {
    /**
     * 生成浏览记录
     */
    int create(History history);

    /**
     * 批量删除浏览记录
     */
    int delete(List<String> ids);

    /**
     * 获取用户浏览历史记录
     */
    List<History> list(Long memberId);
}