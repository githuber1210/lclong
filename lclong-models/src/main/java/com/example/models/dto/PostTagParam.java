package com.example.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PostTagParam", description="标签表")
public class PostTagParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签编号")
    private Long postTagId;

    @ApiModelProperty(value = "标签名称")
    private String description;
}