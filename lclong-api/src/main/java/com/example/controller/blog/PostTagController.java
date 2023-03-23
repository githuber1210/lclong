package com.example.controller.blog;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.result.Result;
import com.example.models.dto.PostTagParam;
import com.example.models.entity.PostTag;
import com.example.framework.service.IPostTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "标签")
@RestController
@RequestMapping("/postTag")
public class PostTagController {

    @Resource
    private IPostTagService postTagService;


    @ApiOperation("添加")
    @PostMapping("/insert")
    public Result insert(@RequestBody PostTagParam postATagParam) {

        LambdaQueryWrapper<PostTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostTag::getDescription, postATagParam.getDescription());

        long count = postTagService.count(queryWrapper);

        if (count > 0) {
            return Result.fail("标签已存在");
        }
        PostTag postTag = new PostTag();
        BeanUtils.copyProperties(postATagParam, postTag);
        return Result.success(postTagService.save(postTag) ? "添加成功" : "添加失败");
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public Result update(@RequestBody PostTagParam postAddTagParam) {
        if (postAddTagParam.getPostTagId() == null) {
            return Result.fail("标签id不能为空");
        }
        PostTag postTag = postTagService.getById(postAddTagParam.getPostTagId());
        if (postTag == null) {
            return Result.fail("标签不存在");
        }

        LambdaQueryWrapper<PostTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostTag::getDescription, postAddTagParam.getDescription());

        long count = postTagService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("标签名称已存在");
        }
        BeanUtils.copyProperties(postAddTagParam, postTag);
        return Result.success(postTagService.updateById(postTag) ? "修改成功" : "修改失败");
    }

    @ApiOperation("删除")
    @GetMapping("/delete")
    public Result delete(Long postTagId) {
        postTagService.removeTag(postTagId);
        return Result.success("删除成功");
    }

    @ApiOperation("根据文章内容获取标签")
    @GetMapping("/getByPostId")
    public Result getByPostId(long objectId) {
        return Result.success(postTagService.getByPostId(objectId));
    }

    @ApiOperation("模糊匹配")
    @GetMapping("/getByName")
    public Result getByName(String keyWord) {

        LambdaQueryWrapper<PostTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(PostTag::getDescription, keyWord + "%");
        return Result.success(postTagService.list(queryWrapper));
    }


    @ApiOperation("分页查询")
    @GetMapping("/queryPageable")
    public Result queryPageable(long pageSize, long page, String tagName) {
        Page<PostTag> postTagPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<PostTag> postTagQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(tagName)) {
            postTagQueryWrapper.like(PostTag::getDescription, "%" + tagName + "%");
        }
        IPage<PostTag> postTagIPage = postTagService.page(postTagPage, postTagQueryWrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("items", postTagIPage.getRecords());
        data.put("total", postTagIPage.getTotal());

        return Result.success(data);
    }
}

