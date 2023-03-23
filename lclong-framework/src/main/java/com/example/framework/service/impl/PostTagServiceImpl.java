package com.example.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.models.dto.PostAddTagParam;
import com.example.models.entity.PostTag;
import com.example.models.entity.PostTagRelation;
import com.example.models.mapper.PostTagMapper;
import com.example.framework.service.IPostTagRelationService;
import com.example.framework.service.IPostTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PostTagServiceImpl extends ServiceImpl<PostTagMapper, PostTag> implements IPostTagService {
    @Resource
    private IPostTagRelationService postTagRelationService;

    @Override
    public boolean savePostTag(PostAddTagParam postAddTagParam) {

        PostTag postTag = new PostTag();
        BeanUtils.copyProperties(postAddTagParam,postTag);

        boolean result = save(postTag);

        if(postAddTagParam.getPostId()!=null){
            PostTagRelation postTagRelation = new PostTagRelation();
            postTagRelation.setPostTagId(postTag.getPostTagId());
            postTagRelation.setPostId(postAddTagParam.getPostId());
            postTagRelation.setTermOrder(postAddTagParam.getTermOrder());
            postTagRelationService.save(postTagRelation);
        }
        return result;
    }

    @Override
    public List<PostTag> getByPostId(Long postId) {
        LambdaQueryWrapper<PostTagRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostTagRelation::getPostId,postId);
        List <PostTagRelation> postTagRelationList = postTagRelationService.list(queryWrapper);

        List<Long> postTagIdList = postTagRelationList.stream().map(PostTagRelation::getPostTagId).collect(Collectors.toList());
        LambdaQueryWrapper<PostTag> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(PostTag::getPostTagId,postTagIdList);

        return this.list(queryWrapper1);
    }

    @Override
    public boolean removeTag(Long postTagId) {
        LambdaQueryWrapper<PostTagRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostTagRelation::getPostTagId,postTagId);
        postTagRelationService.remove(queryWrapper);
        return this.removeById(postTagId);
    }
}
