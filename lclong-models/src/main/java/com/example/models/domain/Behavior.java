package com.example.models.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "user_behavior")
public class Behavior {
    @MongoId
    private String id;

    private String username;

    private String action;

    private String time;

}
