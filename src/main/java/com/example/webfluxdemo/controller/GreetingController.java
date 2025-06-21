package com.example.webfluxdemo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * 第一个 Spring WebFlux Controller.
 * 演示如何返回 Mono 和 Flux.
 */
@RestController
public class GreetingController {

    /**
     * 端点一 (Mono): 返回单个响应式对象
     * @return 一个包含欢迎语的 Mono
     */
    @GetMapping("/hello")
    public Mono<String> getHello() {
        // 返回一个包含单个元素的 Mono。
        // WebFlux 会订阅它，并将结果 "Welcome to the reactive world!" 作为 HTTP 响应体返回。
        return Mono.just("Welcome to the reactive world!");
    }

    /**
     * 端点二 (Flux): 以流的形式持续返回多个对象
     * @return 一个以 Server-Sent Events 形式推送的问候语 Flux
     */
    @GetMapping(value = "/stream-greetings", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamGreetings() {
        // 这里构建了一个响应式流管道：
        return Flux
                // 1. Flux.interval 创建一个从0开始、每隔1秒发出一个递增数字 (Long 类型) 的无限流。
                .interval(Duration.ofSeconds(1))
                // 2. .take(5) 操作符表示我们只取这个无限流中的前5个元素。流将在发出第5个元素后自动完成。
                .take(5)
                // 3. .map 操作符将每个数字 (0, 1, 2, 3, 4) 转换为我们想要的字符串格式。
                .map(i -> "Greeting " + (i + 1));
    }
}