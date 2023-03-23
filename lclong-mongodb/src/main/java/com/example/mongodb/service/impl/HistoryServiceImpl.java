package com.example.mongodb.service.impl;

import com.example.mongodb.domain.History;
import com.example.mongodb.repository.HistoryRepository;
import com.example.mongodb.service.IHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryServiceImpl implements IHistoryService {
    @Resource
    private HistoryRepository historyRepository;

    @Override
    public int create(History history) {
        history.setId(null);
        historyRepository.save(history);
        return 1;
    }

    @Override
    public int delete(List<String> ids){
        List<History> deleteList = new ArrayList<>();
        for(String id:ids){
            History history = new History();
            history.setId(id);
            deleteList.add(history);
        }
        historyRepository.deleteAll(deleteList);
        return ids.size();
    }

    @Override
    public List<History> list(Long memberId) {
        return historyRepository.findByUid(memberId);
    }
}