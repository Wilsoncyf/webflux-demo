package com.example.webfluxdemo.repository;

import com.example.webfluxdemo.entity.Tweet;
import com.example.webfluxdemo.entity.TweetDto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TweetRepository extends ReactiveCrudRepository<Tweet, Integer> {

    // 【核心任务】使用 @Query 实现关联查询
    @Query("""
        SELECT
            t.id as tweet_id, t.content, t.created_at,
            u.id as author_id, u.username as author_username, u.handle as author_handle
        FROM
            tweets t
        INNER JOIN
            users u ON t.user_id = u.id
        WHERE
            u.id = :userId
        ORDER BY
            t.created_at DESC
    """)
    Flux<TweetDto> findTweetsByUserId(Integer userId);
}