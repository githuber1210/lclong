package com.example.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Posts对象", description="文章")
@TableName(autoResultMap = true)
//@Document(indexName = "posts")
public class Posts implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "posts_id")
    @TableId(value = "posts_id", type = IdType.AUTO)
    private Long postsId;

    @ApiModelProperty(value = "对应作者ID")
    private Long postAuthor;

    @ApiModelProperty(value = "发布时间")
    private Date postDate;

    @ApiModelProperty(value = "正文")
    private String postContent;

//    @Field(type = FieldType.Text, analyzer = "ik_max_word")
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

    @ApiModelProperty(value = "文章类型（post/page等）")
    private String postType;

    @ApiModelProperty(value = "评论总数")
    private Long commentCount;

    @ApiModelProperty(value = "浏览量")
    private Long pageView;

    @ApiModelProperty(value = "正文html")
    private String htmlContent;

    private String cover;

}