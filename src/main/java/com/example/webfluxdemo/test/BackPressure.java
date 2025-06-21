package com.example.webfluxdemo.test;


import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

/**
 * @Classname BackPressure
 * @Description TODO
 * @Date 2025/6/21 11:47
 * @Author Wilson Chen
 */
public class BackPressure {
    public static void main(String[] args) {
        //使用 Flux.range(1, 100) 创建一个快速发出100个整数的快速发布者。
        Flux<Integer> range = Flux.range(1, 100);
        //使用 log() 观察内部事件。
        Flux<Integer> log = range.log();
        log.subscribe(new MySubscriber());


        System.out.println("--- 演示 MissingBackpressureException ---");

        Flux<Object> log1 = Flux.create(emitter -> {
                    // 这个发布者逻辑会忽略下游的请求数，疯狂推送数据
                    IntStream.range(1, 1000).forEach(i -> {
                        System.out.println("上游正在推送: " + i);
                        emitter.next(i);
                    });
                    emitter.complete();
                })
                .log();
        log1.subscribe(new MySubscriber());

    }
}
