package com.example.webfluxdemo.entity; // 确保包名正确

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "推文数据传输对象，包含了推文内容和作者的详细信息")
public record TweetDto(

        @Schema(description = "推文的唯一ID", example = "101")
        Integer tweetId,

        @Schema(description = "推文的具体内容", example = "我正在学习响应式编程！")
        String content,

        @Schema(description = "推文的发布时间 (UTC)")
        LocalDateTime createdAt,

        @Schema(description = "作者的用户ID", example = "2")
        Integer authorId,

        @Schema(description = "作者的显示名称", example = "Tony Stark")
        String authorUsername,

        @Schema(description = "作者的唯一handle (@后面的部分)", example = "ironman")
        String authorHandle
) {}