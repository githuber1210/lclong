package com.example.es.service.impl;

import com.example.es.mapper.EsPostMapper;
import com.example.es.config.EsPostRepository;
import com.example.es.service.IEsPostService;
import com.example.es.domain.EsPosts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class EsPostServiceImpl implements IEsPostService {
    @Resource
    private EsPostRepository esPostRepository;

    @Resource
    private EsPostMapper esPostMapper;

    @Override
    public int importAll() {
        List<EsPosts> list = esPostMapper.getAll();
        esPostRepository.saveAll(list);
        return list.size();
    }

    @Override
    public void delete(Long id) {
        log.info("Es delete id {}",id);
        esPostRepository.deleteById(id);
    }

    @Override
    public EsPosts create(Long id) {
        log.info("Es create id {}",id);
        EsPosts post = esPostMapper.selectById(id);
        EsPosts result = esPostRepository.save(post);
        return result;
    }

    @Override
    public Page<EsPosts> search(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return esPostRepository.findByPostTitle(keyword,pageable);
    }
}
