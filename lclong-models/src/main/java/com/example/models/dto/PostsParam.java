package com.example.models.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PostsParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    private Long postsId;

    @ApiModelProperty(value = "时间")
    private Date postDate;

    @ApiModelProperty(value = "内容")
    private String postContent;

    @ApiModelProperty(value = "正文--html")
    private String htmlContent;

    @ApiModelProperty(value = "标题")
    private String postTitle;

    @ApiModelProperty(value = "摘要")
    private String postExcerpt;

    @ApiModelProperty("状态")
    private String postStatus;

    @ApiModelProperty("排序")
    private Integer menuOrder;

    @ApiModelProperty("专栏")
    private Long termTaxonomyId;

    @ApiModelProperty("标签")
    private String tags;

    @ApiModelProperty("封面")
    private String cover;
}
