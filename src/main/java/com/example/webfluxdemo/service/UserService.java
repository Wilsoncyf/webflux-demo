package com.example.webfluxdemo.service;

import com.example.webfluxdemo.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public UserService(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    // 【核心任务1】实现动态查询方法
    public Flux<User> searchUsers(String username, String handle, int page, int size, String sortField, Sort.Direction sortDirection) {
        // 1. 手动构建 Sort 对象
        Sort sort = Sort.by(sortDirection, sortField);

        // 2. 手动构建 Pageable 对象，它是 PageRequest 的实例
        Pageable pageable = PageRequest.of(page, size, sort);

        // 3. 后续逻辑不变，直接使用构建好的 Pageable 对象
        Criteria criteria = buildUserSearchCriteria(username, handle);
        Query query = Query.query(criteria).with(pageable);

        return r2dbcEntityTemplate.select(query, User.class);
    }

    // 【核心任务2】实现动态计数方法
    public Mono<Long> countUsers(String username, String handle) {
        Criteria criteria = buildUserSearchCriteria(username, handle);
        return r2dbcEntityTemplate.count(Query.query(criteria), User.class);
    }

    // 这是一个私有的辅助方法，专门用于根据输入参数构建 Criteria
    private Criteria buildUserSearchCriteria(String username, String handle) {
        Criteria criteria = Criteria.empty();
        if (StringUtils.hasText(username)) {
             criteria = criteria.and("username").like("%" + username + "%");
        }
        if (StringUtils.hasText(handle)) {
            criteria = criteria.and("handle").is(handle);
        }
        return criteria;
    }

    public Mono<Long> updateUserBio(Integer userId, String newBio) {
        // 1. 创建 Query，定位到要更新的用户
        Query query = Query.query(Criteria.where("id").is(userId));

        // 2. 创建 Update，指定要更新的字段和新值
        Update update = Update.update("bio", newBio);

        // 3. 执行局部更新，并返回影响的行数
        return r2dbcEntityTemplate.update(query, update, User.class);
    }
}