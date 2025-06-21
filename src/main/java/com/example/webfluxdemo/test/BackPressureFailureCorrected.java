package com.example.webfluxdemo.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BackPressureFailureCorrected {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- 演示 MissingBackpressureException (最终修正版) ---");

        Flux.create(emitter -> {
            // 关键：在一个独立的线程中运行失控的生产者
            var executor = Executors.newSingleThreadScheduledExecutor();
            executor.execute(() -> {
                IntStream.range(1, 1000).forEach(i -> {
                    if (emitter.isCancelled()) {
                        return;
                    }
                    System.out.println("上游线程 [" + Thread.currentThread().getName() + "] 正在推送: " + i);
                    emitter.next(i);
                    try {
                        // 稍微减慢一点速度，便于观察
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                emitter.complete();
            });

        }, FluxSink.OverflowStrategy.ERROR) // 我们依然坚持报错策略
                .log()
                .subscribe(new MySubscriber()); // 慢速消费者依然运行在 main 线程

        // 等待足够长的时间以便观察到错误
        TimeUnit.SECONDS.sleep(5);
    }
}

