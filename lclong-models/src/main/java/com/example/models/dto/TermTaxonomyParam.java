package com.example.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TermTaxonomy对象", description="栏目")
public class TermTaxonomyParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")

    private Long termTaxonomyId;

    @ApiModelProperty(value = "说明")
    private String description;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "父栏目id")
    private Long parentId;

    @ApiModelProperty("属性")
    private String cover;

    private String type;
}
