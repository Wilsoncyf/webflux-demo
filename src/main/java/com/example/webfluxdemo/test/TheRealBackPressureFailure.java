package com.example.webfluxdemo.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class TheRealBackPressureFailure {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- 演示 MissingBackpressureException (最终正确版) ---");

        Flux.create(emitter -> {
            // 这个生产者现在将被 subscribeOn 调度到 "producer-thread"
            IntStream.range(1, 1000).forEach(i -> {
                System.out.println("上游线程 [" + Thread.currentThread().getName() + "] 正在推送: " + i);
                emitter.next(i);
            });
            emitter.complete();
        }, FluxSink.OverflowStrategy.ERROR)
        .log()
        // 1. 将整个上游（包括源头的创建和推送）调度到一个独立的生产者线程
        .subscribeOn(Schedulers.newSingle("producer-thread"))
        // 2. 将整个下游（从这里开始）调度到另一个独立的消费者线程
        .publishOn(Schedulers.newSingle("consumer-thread"))
        // 3. 慢速消费者现在将在 "consumer-thread" 上执行
        .subscribe(new MySubscriber());

        // 等待，以便观察异步线程的执行结果
        TimeUnit.SECONDS.sleep(3);
    }
}

