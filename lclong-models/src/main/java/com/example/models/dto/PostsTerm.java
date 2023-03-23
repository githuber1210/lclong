package com.example.models.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostsTerm implements Serializable {

    private Long[] postsIds;

    private Long[] termTaxonomyIds;
}
