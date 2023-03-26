package com.example.mongodb.repository;


import com.example.mongodb.domain.History;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends MongoRepository<History,String> {
    List<History> findByUid(Long id);
}