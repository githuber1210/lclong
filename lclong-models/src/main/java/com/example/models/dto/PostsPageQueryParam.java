package com.example.models.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PostsPageQueryParam {
    @ApiModelProperty(value = "排序字段,按数据库字段:menu_order,post_date,post_modified,page_view;多个以逗号间隔")
    private String orderBy;
    @ApiModelProperty(value = "是否升序-----boolean")
    private boolean asc;
    @ApiModelProperty(value = "栏目id----非必填")
    private Long termTaxonomyId;
    @ApiModelProperty(value = "标题关键字")
    private String postTitleKeyword;
    @ApiModelProperty(value = "要查询标签----id")
    private Long searchTagId;
    @ApiModelProperty(value = "文章状态----PUBLISHED,DELETED,DRAFT")
    private String postStatus;
    @ApiModelProperty(value = "页面显示数量")
    long pageSize;
    @ApiModelProperty(value = "页码")
    long page;


}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        