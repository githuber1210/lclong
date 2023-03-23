package com.example.mongodb.repository;


import com.example.mongodb.domain.History;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoryRepository extends MongoRepository<History,String> {
    List<History> findByUid(Long memberId);
}