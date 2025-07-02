package com.example.webfluxdemo.controller;

import com.example.webfluxdemo.entity.UpdateBioRequest;
import com.example.webfluxdemo.entity.User;
import com.example.webfluxdemo.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController //RestController的作用
@RequestMapping("/api/users")//RequestMapping的作用
public class UserController {

    private final UserService userService;

    // 这个函数在这里的作用 是将UserService对象注入到UserController中，使用的是 构造函数注入
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public Flux<User> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String handle,
            // 接收原始的分页和排序参数，并提供默认值
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection
    ) {
        return userService.searchUsers(username, handle, page, size, sortField, sortDirection);
    }

    @PatchMapping("/{userId}/bio")
    public Mono<ResponseEntity<Void>> updateUserBio(
            @PathVariable Integer userId,
            @RequestBody UpdateBioRequest request
    ) {
        return userService.updateUserBio(userId, request.bio())
                .map(updatedCount -> updatedCount > 0 ?
                        ResponseEntity.ok().build() :       // 更新成功 (影响行数>0)，返回 200 OK
                        ResponseEntity.notFound().build()); // 未找到用户 (影响行数=0)，返回 404 Not Found
    }
}