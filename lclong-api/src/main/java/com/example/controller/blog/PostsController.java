package com.example.controller.blog;

import com.example.common.result.Result;
import com.example.models.dto.PostsPageQueryParam;
import com.example.models.dto.PostsParam;
import com.example.models.vo.PostsVo;
import com.example.framework.service.IPostsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(tags = "博客管理")
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Resource
    private IPostsService postsService;

    @ApiOperation("分页查询")
    @GetMapping("/postPageQuery")
    public Result page(PostsPageQueryParam postsPageQueryParam) {
        Map<String, Object> data = new HashMap<>();
        List<PostsVo> postsIPage = postsService.findByPageWithTagPaged(postsPageQueryParam);
        data.put("items", postsIPage);
        return Result.success(data);
    }

    @ApiOperation("添加")
    @PostMapping("/insert")
    public Result insert(@RequestBody PostsParam postsParam) {
        postsService.savePosts(postsParam);
        return Result.success();
    }

    @ApiOperation("根据编号查询")
    @GetMapping("/getById")
    public Result getById(long postsId) {
        return Result.success(postsService.getPostsById(postsId));
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public Result update(@RequestBody PostsParam postsParam) {
        postsService.updatePosts(postsParam);
        return Result.success();
    }
    @ApiOperation("删除")
    @GetMapping("/delete")
    public Result delete(long postsId) {
        postsService.setDeleted(postsId);
        return Result.success();
    }

    @ApiOperation("置顶")
    @PostMapping("/setOnTop/{postsId}")
    public Result setOnTop(@PathVariable Long postsId) {
        postsService.setOnTop(postsId, 1);
        return Result.success();
    }

    @ApiOperation("取消置顶")
    @PostMapping("/cancelOnTop/{postsId}")
    public Result cancelOnTop(@PathVariable Long postsId) {
        postsService.setOnTop(postsId, 0);
        return Result.success();
    }
}

