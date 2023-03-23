package com.example.mongodb.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Data
@Accessors(chain = true)
public class History {
    @MongoId
    private String id;
    @Indexed
    private Long uid;
    @Indexed
    private Long postId;

}