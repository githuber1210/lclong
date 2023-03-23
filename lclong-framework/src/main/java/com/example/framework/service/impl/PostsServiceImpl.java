package com.example.framework.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.exception.ServiceException;
import com.example.common.result.ResultCode;
import com.example.framework.quartz.service.IScheduleService;
import com.example.framework.quartz.job.PublishPostJob;
import com.example.framework.service.IPostTagRelationService;
import com.example.framework.service.IPostTagService;
import com.example.framework.service.IPostsService;
import com.example.framework.service.IUserService;
import com.example.models.dto.PostsPageQueryParam;
import com.example.models.dto.PostsParam;
import com.example.models.entity.PostTag;
import com.example.models.entity.PostTagRelation;
import com.example.models.entity.Posts;
import com.example.models.entity.User;
import com.example.models.mapper.PostsMapper;
import com.example.models.state.PostStatus;
import com.example.models.vo.PostsVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@Slf4j
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements IPostsService {
    @Resource
    private IUserService userService;
    @Resource
    private IPostTagService postTagService;
    @Resource
    private PostsMapper postsMapper;
    @Resource
    private IPostTagRelationService postTagRelationService;

    @Resource
    private IScheduleService scheduleService;



    // 匹配图片的 markdown 语法  ![xx](hhhx.png?ax)
    public static final String IMG_PATTERN = "\\!\\[(.*)\\]\\((.*)\\)";

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<PostsVo> findByPageWithTagPaged(PostsPageQueryParam postsPageQueryParam) {

        QueryWrapper<PostsPageQueryParam> queryWrapper = new QueryWrapper<>();

        if(StringUtils.isNotBlank(postsPageQueryParam.getPostTitleKeyword())){
            queryWrapper.like("posts.post_title", "%"+postsPageQueryParam.getPostTitleKeyword()+"%");
        }
        if (postsPageQueryParam.getOrderBy() != null) {
            String[] cloums = postsPageQueryParam.getOrderBy().split(",");
            queryWrapper.orderBy(true, postsPageQueryParam.isAsc(), Arrays.asList(cloums));
        }
        queryWrapper.eq("posts.post_status", postsPageQueryParam.getPostStatus());

        long pageSize = postsPageQueryParam.getPageSize();
        long pageStart = (postsPageQueryParam.getPage() - 1) * pageSize;

        Long searchTagId = postsPageQueryParam.getSearchTagId();

        return this.getBaseMapper().findByPageWithTagPaged(queryWrapper, searchTagId, pageStart, pageSize);
    }

    @Override
    @Transactional
    public void savePosts(PostsParam postsParam) {
        Posts posts = new Posts();
        BeanUtils.copyProperties(postsParam, posts);

        boolean needScheduleAfter = handleScheduledBefore(postsParam.getPostDate(), posts);

        posts.setCommentCount(0L);

        posts.setPostAuthor(userService.getCurrentLoginUser().getId());

        handleContentImg(posts);

        postsMapper.insert(posts);

        insertOrUpdateTag(postsParam.getTags(), posts.getPostsId());

        if (needScheduleAfter) {
            handleScheduledAfter(posts);
        }
    }

    @Override
    @Transactional
    public void updatePosts(PostsParam postsParam) {
        if (postsParam.getPostsId() == null) {
            throw new ServiceException(ResultCode.SERVICE_ERROR,"ID为空!");
        }

        Posts posts = getById(postsParam.getPostsId());
        if (posts == null) {
            throw new ServiceException(ResultCode.SERVICE_ERROR,"找不到该博客!");
        }

        BeanUtils.copyProperties(postsParam, posts);

        handleContentImg(posts);
        updateById(posts);

        insertOrUpdateTag(postsParam.getTags(), posts.getPostsId());

    }

    @Override
    public void setDeleted(long postsId) {
        Posts posts = getById(postsId);
        if (posts == null) {
            throw new ServiceException(ResultCode.SERVICE_ERROR,"找不到该博客!");
        }
        posts.setPostStatus("DELETED");
        updateById(posts);
    }

    private void insertOrUpdateTag(String tags, Long post_Id) {

        LambdaQueryWrapper<PostTagRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostTagRelation::getPostId, post_Id);
        postTagRelationService.remove(queryWrapper);

        if (StringUtils.isBlank(tags)) {
            return;
        }

        int order = 0;
        for (String tag : tags.split(",")) {

            LambdaQueryWrapper<PostTag> postTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            postTagLambdaQueryWrapper.eq(PostTag::getPostTagId, tag);
            List<PostTag> tagList = postTagService.list(postTagLambdaQueryWrapper);

            PostTagRelation postTagRelation = new PostTagRelation();

            postTagRelation.setPostTagId(tagList.get(0).getPostTagId());
            postTagRelation.setPostId(post_Id);
            postTagRelation.setTermOrder(order);

            postTagRelationService.save(postTagRelation);

            order++;
        }
    }

    @Override
    public void setOnTop(Long postsId, Integer flag) {
        Posts article = getById(postsId);
        article.setMenuOrder(flag);
        updateById(article);
    }

    @Override
    public boolean updatePostByScheduler(Long postId) {
        log.info("更新文章{}状态", postId);
        // 根据文章 ID 获取文章
        Posts posts = getById(postId);
        if (posts == null) {
            log.error("文章定时发布出错，文章 ID 不存在");
            return false;
        }

        // 文章设置的发布时间
        posts.setPostModified(DateTime.now());
        // 更新发布状态
        posts.setPostStatus(PostStatus.PUBLISHED.name());

        return updateById(posts);

    }

    @Override
    public void removePostsById(Long id) {
        LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Posts::getPostsId,id);
        postsMapper.delete(queryWrapper);
    }

    @Override
    public PostsVo getPostsById(Long id) {
        Posts posts = this.getById(id);
        PostsVo postsVo = new PostsVo();
        if (posts == null) {
            return postsVo;
        }

        // 转成 VO
        BeanUtils.copyProperties(posts, postsVo);

        LambdaQueryWrapper<PostTagRelation> tagRelationWrapper = new LambdaQueryWrapper<>();
        tagRelationWrapper.eq(PostTagRelation::getPostId, posts.getPostsId());
        tagRelationWrapper.orderBy(true,true,PostTagRelation::getTermOrder);

        List<PostTagRelation> postTagRelationList = postTagRelationService.list(tagRelationWrapper);

        if (postTagRelationList.size() > 0) {
            // 取出标签 ID
            List<Long> tagIds = postTagRelationList.stream().map(PostTagRelation::getPostTagId).collect(Collectors.toList());

            LambdaQueryWrapper<PostTag> tagQuery = new LambdaQueryWrapper<>();
            tagQuery.in(PostTag::getPostTagId, tagIds);
            List<PostTag> postTags = postTagService.list(tagQuery);

            Collections.sort(postTags, new Comparator<PostTag>() {
                @Override
                public int compare(PostTag o1, PostTag o2) {
                    return tagIds.indexOf(o1.getPostTagId()) - tagIds.indexOf(o2.getPostTagId());
                }
            });

            postsVo.setTagsName(StringUtils.join(postTags.stream().map(PostTag::getPostTagId).collect(Collectors.toList()), ","));
        }

        User users = userService.getById(posts.getPostAuthor());
        postsVo.setUsername(users.getUsername());
        return postsVo;
    }


    /**
     * 对外链图片进行转链
     *
     * @param posts
     */
    private void handleContentImg(Posts posts) {
        String content = posts.getPostContent();
        String htmlContent = posts.getHtmlContent();

        if (StringUtils.isBlank(content) || StringUtils.isBlank(htmlContent)) {
            return;
        }

        htmlContent = StringEscapeUtils.unescapeHtml4(htmlContent);

        Pattern p = Pattern.compile(IMG_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(content);

        Map<String, Future<String>> map = new HashMap<>();

        while (m.find()) {
            String imageName = m.group(1);
            String imageUrl = m.group(2);

            log.info("使用分组进行替换图片名字：{}，图片路径：{}", imageName, imageUrl);

        }
        for (String oldUrl : map.keySet()) {
            Future<String> future = map.get(oldUrl);

            String imageUrl = null;
            try {
                imageUrl = future.get();
            } catch (InterruptedException | ExecutionException e) {
               throw new ServiceException(ResultCode.SERVICE_ERROR,"图片转链失败");
            }
            content = content.replace(oldUrl, imageUrl);

            if (StringUtils.isNotBlank(htmlContent)) {
                htmlContent = htmlContent.replace(oldUrl, imageUrl);
            }
        }
        posts.setPostContent(content);
        posts.setHtmlContent(htmlContent);
    }

    private void handleScheduledAfter(Posts posts) {
        // 文章已经保存为草稿了，并且拿到了文章 ID   调用定时任务
        String jobName = scheduleService.scheduleFixTimeJob(PublishPostJob.class, posts.getPostDate(), posts.getPostsId().toString());
        log.debug("定时任务{}开始执行", jobName);
    }

    private boolean handleScheduledBefore(Date postDate, Posts posts) {


        if (postDate != null) {
            log.debug("定时发布，时间{}", DateUtil.formatDateTime(postDate));

            // 定时任务的时间必须大于当前时间 10 分钟
//            if (DateUtil.between(DateTime.now(), postDate, DateUnit.MINUTE, false) <= postScheduleMinInterval) {
//                Asserts.fail("定时发布的时间必须在 "+ postScheduleMinInterval +" 分钟后");
//            }

            posts.setPostStatus(PostStatus.DRAFT.name());
            return true;
        } else {
            // 默认设置发布时间，方便排序
            posts.setPostModified(DateTime.now());
        }
        return false;
    }
}
