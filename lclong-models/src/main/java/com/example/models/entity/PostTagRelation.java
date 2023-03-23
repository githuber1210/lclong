
package com.example.models.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PostTagRelation对象", description="标签文章关系表")
public class PostTagRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "对应文章ID")
    private Long postId;

    @ApiModelProperty(value = "标签ID")
    private Long postTagId;

    @ApiModelProperty(value = "排序")
    private Integer termOrder;


}