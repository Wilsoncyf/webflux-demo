package com.example.webfluxdemo.controller;

import com.example.webfluxdemo.entity.PostTweetRequest;
import com.example.webfluxdemo.entity.Tweet;
import com.example.webfluxdemo.entity.TweetDto;
import com.example.webfluxdemo.service.TweetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tweets")
public class TweetController {

    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    // 获取特定用户发布的所有推文
    @Operation(summary = "获取指定用户发布的所有推文", description = "根据用户ID，返回一个包含该用户所有推文的JSON数组。")
    @GetMapping("/by-user/{userId}")
    public Flux<TweetDto> getTweetsByUserId(
            @Parameter(description = "用户的唯一ID", required = true, example = "2") @PathVariable Integer userId) {
        return tweetService.getTweetsByAuthorId(userId);
    }

    // 获取特定用户的时间线（包含他所关注的人的推文）
    // 我们将它做成一个流式API (Server-Sent Events)
    @Operation(summary = "获取指定用户的时间线", description = "以流式（Server-Sent Events）返回该用户所关注的人发布的推文。")
    @GetMapping(value = "/timeline/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TweetDto> getTimelineForUser(
            @Parameter(description = "用户的唯一ID", required = true, example = "1") @PathVariable Integer userId) {
        return tweetService.getTimelineForUser(userId);
    }

    @PostMapping
    public Mono<Tweet> postNewTweet(@RequestBody PostTweetRequest request) {
        // 为了演示，我们在这里创建一个新的 Tweet 实体
        // 在真实应用中，创建时间等字段通常由数据库或业务逻辑处理
        Tweet newTweet = new Tweet(null, request.userId(), request.content(), java.time.LocalDateTime.now());
        return tweetService.postNewTweet(newTweet);
    }
}