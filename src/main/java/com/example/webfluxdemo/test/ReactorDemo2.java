package com.example.webfluxdemo.test;


import reactor.core.publisher.Flux;

/**
 * @Classname ReactorDemo2
 * @Description TODO
 * @Date 2025/6/20 23:22
 * @Author Wilson Chen
 */
public class ReactorDemo2 {
    public static void main(String[] args) {
        Flux<String> just = Flux.just("Peter", "Ben", "Anna", "Steve");
        just.subscribe(System.out::println);
    }
}
