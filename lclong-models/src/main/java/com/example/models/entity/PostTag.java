
package com.example.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PostTag 标签对象", description="标签表")
public class PostTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签编号")
    @TableId(value = "post_tag_id", type = IdType.AUTO)
    private Long postTagId;

    @ApiModelProperty(value = "标签名称")
    private String description;
    
}