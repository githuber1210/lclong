package com.example.mongodb.repository;

import com.example.mongodb.domain.UserBehavior;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBehaviorRepository extends MongoRepository<UserBehavior, String> {
}
