package com.example.es.config;

import com.example.es.domain.EsPosts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsPostRepository extends ElasticsearchRepository<EsPosts, Long> {

    Page<EsPosts> findByPostTitle(String postTitle, Pageable pageable);
    Page<EsPosts> findByPostTitleOrPostContent(String postTitle,String postContent,Pageable pageable);
}
