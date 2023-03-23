package com.example.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.models.dto.PostsPageQueryParam;
import com.example.models.dto.PostsParam;
import com.example.models.entity.Posts;
import com.example.models.vo.PostsVo;

import java.util.List;

public interface IPostsService extends IService<Posts> {


    List<PostsVo> findByPageWithTagPaged(PostsPageQueryParam postsPageQueryParam);

    void savePosts(PostsParam postsParam);

    void updatePosts(PostsParam postsParam);

    void setDeleted(long postsId);

    void removePostsById(Long id);

    PostsVo getPostsById(Long id);

    void setOnTop(Long postsId, Integer flag);

    boolean updatePostByScheduler(Long postId);

}
