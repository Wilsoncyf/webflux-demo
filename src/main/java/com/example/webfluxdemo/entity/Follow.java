package com.example.webfluxdemo.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @Classname User
 * @Description TODO
 * @Date 2025/6/21 22:19
 * @Author Wilson Chen
 */
@Table("follows")
public record Follow(@Column("follower_id") Integer followerId, @Column("following_id") Integer followingId) {}
