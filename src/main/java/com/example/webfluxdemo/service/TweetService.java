package com.example.webfluxdemo.service;

import com.example.webfluxdemo.entity.TweetDto;
import com.example.webfluxdemo.repository.FollowRepository;
import com.example.webfluxdemo.repository.TweetRepository;
import com.example.webfluxdemo.repository.UserRepository;
import com.example.webfluxdemo.entity.Follow;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public TweetService(TweetRepository tweetRepository, UserRepository userRepository, FollowRepository followRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
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
}