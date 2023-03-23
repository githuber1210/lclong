package com.example.models.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.example.models.dto.PostsPageQueryParam;
import com.example.models.entity.Posts;
import com.example.models.vo.PostsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostsMapper extends BaseMapper<Posts> {

    List<PostsVo> findByPageWithTagPaged(@Param(Constants.WRAPPER) Wrapper<PostsPageQueryParam> wrapper,
                                         Long searchTagId,
                                         long pageStart,
                                         long pageSize);

}