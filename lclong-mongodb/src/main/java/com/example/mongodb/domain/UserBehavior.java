package com.example.mongodb.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
@Document(collection = "user_behavior")
public class UserBehavior {
    @MongoId
    private String id;

    private String userId;

    private String action;

    private Date timestamp;

}
