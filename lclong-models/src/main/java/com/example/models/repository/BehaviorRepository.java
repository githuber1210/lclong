package com.example.models.repository;

import com.example.models.domain.Behavior;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BehaviorRepository extends MongoRepository<Behavior, String> {

    List<Behavior> findByUsername(String username);

    void deleteAllByUsername(String username);
}
