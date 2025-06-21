package com.example.webfluxdemo.test;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulerDemo {
    public static void main(String[] args) throws InterruptedException {
        Flux.range(1, 3)
            .log()
            .map(i -> {
                System.out.println("Map 1 - " + Thread.currentThread().getName() + "    "+i);
                return i * 10;
            })
            .subscribeOn(Schedulers.boundedElastic()) // 指定源头在 boundedElastic 线程池上执行
            .map(i -> {
                System.out.println("Map 2 - " + Thread.currentThread().getName() + "    "+i);
                return "Value " + i;
            })
            .publishOn(Schedulers.parallel()) // 从这里开始，切换到 parallel 线程池
            .map(s -> {
                System.out.println("Map 3 - " + Thread.currentThread().getName());
                return s.toLowerCase();
            })
            .subscribe(v -> System.out.println("Received: " + v + " - on thread " + Thread.currentThread().getName()));

        Thread.sleep(500); // 等待异步操作完成
    }
}

