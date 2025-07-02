// entity/PostTweetRequest.java
package com.example.webfluxdemo.entity;

public record PostTweetRequest(Integer userId, String content) {}