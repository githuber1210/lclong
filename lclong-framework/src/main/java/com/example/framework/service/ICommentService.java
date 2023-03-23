package com.example.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.models.entity.Comment;

import java.util.List;

public interface ICommentService extends IService<Comment> {

    List<Comment> findCommentDetail(Integer articleId);
}
