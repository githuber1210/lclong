package com.example.controller.system.common;


import cn.hutool.core.date.DateUtil;
import com.example.common.result.Result;
import com.example.models.entity.Comment;
import com.example.framework.service.ICommentService;
import com.example.framework.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = "通告评论模块")
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService commentService;

    @Resource
    private IUserService userService;

    @ApiOperation("列出所有评论")
    @GetMapping
    public Result list() {
        List<Comment> commentList = commentService.list();
        return Result.success(commentList);
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/{id}")
    public Result removeById(@PathVariable Integer id) {
        commentService.removeById(id);
        return Result.success();
    }

    @ApiOperation("添加评论")
    @PostMapping
    public Result save(@RequestBody Comment comment) {
        if (comment.getId() == null) {
            comment.setUid(userService.getCurrentLoginUser().getId());
            comment.setTime(DateUtil.now());
            if (comment.getPid() != null) {
                Integer pid = comment.getPid();
                Comment pComment = commentService.getById(pid);
                if (pComment.getPpid() != null) {
                    comment.setPpid(pComment.getPpid());
                } else {
                    comment.setPpid(comment.getPid());
                }
            }
        }
        commentService.save(comment);
        return Result.success();
    }

    @ApiOperation("获取评论树结构")
    @GetMapping("/tree/{noticeId}")
    public Result listTree(@PathVariable Integer noticeId) {
        List<Comment> commentList = commentService.findCommentDetail(noticeId);
        List<Comment> originList = commentList.stream()
                .filter(comment -> comment.getPpid() == null)
                .collect(Collectors.toList());
        // 设置评论数据的子节点，也就是回复内容
        for (Comment origin : originList) {
            // 表示回复对象集合
            List<Comment> comments = commentList.stream()
                    .filter(comment -> origin.getId().equals(comment.getPpid()))
                    .collect(Collectors.toList());
            comments.forEach(comment -> {
                // 找到当前评论的父级
                Optional<Comment> pComment = commentList.stream()
                        .filter(c1 -> c1.getId().equals(comment.getPid()))
                        .findFirst();
                pComment.ifPresent((v -> {
                    comment.setPUid(v.getUid());
                    comment.setPUname(v.getUsername());
                }));
            });
            origin.setChildren(comments);
        }
        return Result.success(originList);
    }

}

