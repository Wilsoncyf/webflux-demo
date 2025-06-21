package com.example.webfluxdemo.repository;

import com.example.webfluxdemo.entity.Follow;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface FollowRepository extends ReactiveCrudRepository<Follow, Integer> {
    // 根据粉丝ID，查找所有他关注的关系
    Flux<Follow> findAllByFollowerId(Integer followerId);
}