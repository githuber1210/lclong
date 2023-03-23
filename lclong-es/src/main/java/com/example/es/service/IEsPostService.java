package com.example.es.service;

import com.example.es.domain.EsPosts;
import org.springframework.data.domain.Page;

public interface IEsPostService {
    /**
     * 从数据库导入所有文章到 ES
     * @return
     */
    int importAll();

    void delete(Long id);

    EsPosts create(Long id);

    /**
     * 根据关键字查询
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<EsPosts> search(String keyword, Integer pageNum, Integer pageSize);
}