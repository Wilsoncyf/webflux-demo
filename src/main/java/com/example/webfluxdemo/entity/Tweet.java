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
@Table("tweets")
public record Tweet(@Id Integer id, @Column("user_id") Integer userId, String content, java.time.LocalDateTime createdAt) {}