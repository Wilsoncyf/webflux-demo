package com.example.webfluxdemo.repository;

import com.example.webfluxdemo.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
    // Spring Data R2DBC 会自动实现这个方法
}