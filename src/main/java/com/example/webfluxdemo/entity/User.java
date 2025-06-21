package com.example.webfluxdemo.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @Classname User
 * @Description TODO
 * @Date 2025/6/21 22:19
 * @Author Wilson Chen
 */
@Table("users")
public record User(@Id Integer id, String username, String handle) {}
