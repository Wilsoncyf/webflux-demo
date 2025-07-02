package com.example.webfluxdemo.service;

import com.example.webfluxdemo.entity.Tweet;
import com.example.webfluxdemo.entity.TweetDto;
import com.example.webfluxdemo.entity.User;
import com.example.webfluxdemo.repository.FollowRepository;
import com.example.webfluxdemo.repository.TweetRepository;
import com.example.webfluxdemo.repository.UserRepository;
import com.example.webfluxdemo.entity.Follow;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final R2dbcEntityTemplate  r2dbcEntityTemplate;

    public TweetService(TweetRepository tweetRepository,
                        UserRepository userRepository,
                        FollowRepository followRepository,
                        R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    // 简单业务：直接调用 Repository 的自定义查询
    public Flux<TweetDto> getTweetsByAuthorId(Integer userId) {
        return tweetRepository.findTweetsByUserId(userId);
    }

    // 【核心任务】复杂业务：为用户生成时间线
    public Flux<TweetDto> getTimelineForUser(Integer userId) {
        // 这里的逻辑是响应式编程的精髓：一个由多个步骤组成的声明式管道
        
        // 1. 首先，根据用户ID，从 follows 表中找到他所有关注的人
        return followRepository.findAllByFollowerId(userId)
                // 2. 对于每一个关注关系(Follow)，我们只关心被关注者的ID (followingId)
                .map(Follow::followingId)
                // 3. 【关键】对于每一个被关注者的ID，我们异步地去查询这个ID发布的所有推文。
                //    这是一个 1 -> N 的转换（一个ID 对应 一个推文流 Flux<TweetDto>），
                //    所以必须使用 flatMap。
                //    flatMap 会将所有这些子流（每个被关注者的推文流）合并成一个大的、统一的推文流。
                .flatMap(followingId -> tweetRepository.findTweetsByUserId(followingId))
                // 4. 最后，为了让时间线看起来正确，我们将所有合并后的推文按创建时间倒序排列。
                .sort((dto1, dto2) -> dto2.createdAt().compareTo(dto1.createdAt()));


    }

    /**
     * 发布一篇新推文，并更新用户的个人简介来记录此事。
     * 这是一个包含两次写入的操作，因此必须是事务性的。
     * @param newTweet 要保存的推文实体
     * @return 保存成功后的推文实体
     */
    @Transactional // <-- 【核心】将整个响应式流包裹在一个事务中
    public Mono<Tweet> postNewTweet(Tweet newTweet) {
        // 1. 保存新推文
        return tweetRepository.save(newTweet)
                // 2. 使用 flatMap 链接下一个数据库写操作
                .flatMap(savedTweet -> {
                    // 3. 更新用户的 bio 字段为 "Last tweet: [推文内容]"
                    Query userQuery = Query.query(Criteria.where("id").is(savedTweet.userId()));
                    Update userUpdate = Update.update("bio", "Last tweet: " + savedTweet.content());

                    // r2dbcEntityTemplate.update(...) 返回的是 Mono<Integer> (影响行数)
                    // 我们执行它，但在链的最后返回原始的 savedTweet
                    return r2dbcEntityTemplate.update(userQuery, userUpdate, User.class)
                            .thenReturn(savedTweet); // thenReturn 会在 update 成功后，将流的信号替换为 savedTweet
                });
    }
}