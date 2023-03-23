package com.example.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.es.domain.EsPosts;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EsPostMapper extends BaseMapper<EsPosts> {
    @Select("SELECT * FROM posts")
    List<EsPosts> getAll();
}