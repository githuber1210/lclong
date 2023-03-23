package com.example.models.vo;

import com.example.models.entity.PostTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostsVo extends BaseVO {

    @ApiModelProperty(value = "编号")
    private Long postsId;
    @ApiModelProperty(value = "作者编号")
    private Long postAuthor;
    @ApiModelProperty(value = "发布时间")
    private Date postDate;
    @ApiModelProperty(value = "正文")
    private String postContent;
    @ApiModelProperty(value = "正文 ----html")
    private String htmlContent;
    @ApiModelProperty(value = "标题")
    private String postTitle;
    @ApiModelProperty(value = "摘录")
    private String postExcerpt;
    @ApiModelProperty(value = "文章状态")
    private String postStatus;
    @ApiModelProperty(value = "评论状态")
    private String commentStatus;
    @ApiModelProperty(value = "修改时间")
    private Date postModified;
    @ApiModelProperty(value = "排序ID")
    private Integer menuOrder;
    @ApiModelProperty(value = "评论总数")
    private Long commentCount;
    @ApiModelProperty(value = "发布人")
    private String username;
    @ApiModelProperty(value = "栏目编号")
    private Long termTaxonomyId;
    @ApiModelProperty(value = "标签")
    private String tagsName;
    @ApiModelProperty(value = "标签集合")
    private List<PostTag> tags;
    @ApiModelProperty("封面")
    private String cover;
    @ApiModelProperty("浏览量")
    private Long pageView;
    @ApiModelProperty("点赞")
    private Long likeCount;
    @ApiModelProperty("格式化修改时间")
    private String postModifiedShortTime;

}
