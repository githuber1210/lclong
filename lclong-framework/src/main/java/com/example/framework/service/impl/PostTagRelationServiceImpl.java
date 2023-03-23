package com.example.framework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.models.entity.PostTagRelation;
import com.example.models.mapper.PostTagRelationMapper;
import com.example.framework.service.IPostTagRelationService;
import org.springframework.stereotype.Service;

@Service
public class PostTagRelationServiceImpl extends ServiceImpl<PostTagRelationMapper, PostTagRelation> implements IPostTagRelationService {

}