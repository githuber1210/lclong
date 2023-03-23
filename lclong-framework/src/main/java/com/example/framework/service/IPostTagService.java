package com.example.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.models.dto.PostAddTagParam;
import com.example.models.entity.PostTag;

import java.util.List;

public interface IPostTagService extends IService<PostTag> {

    boolean savePostTag(PostAddTagParam postAddTagParam);

    /**
     * 获取文章标签
     * @param postId
     * @return
     */
    List<PostTag> getByPostId(Long postId);
    /**
     * 删除标签
     * @param postTagId
     * @return
     */
    boolean removeTag(Long postTagId);
}