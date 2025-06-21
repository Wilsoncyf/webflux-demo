package com.example.webfluxdemo.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BackPressureSolved {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- 使用 subscribeOn 解决问题 ---");

        Flux.create(emitter -> {
            // 这个生产者现在将被 subscribeOn 调度到 Schedulers.parallel() 的某个线程上
            IntStream.range(1, 1000).forEach(i -> {
                System.out.println("上游线程 [" + Thread.currentThread().getName() + "] 正在推送: " + i);
                emitter.next(i);
                // 这里我们不再需要手写休眠，让它尽情地跑
            });
            emitter.complete();
        }, FluxSink.OverflowStrategy.ERROR)
        .log()
        // **解决方案**: 将整个上游（包括源头的创建和推送）调度到一个独立的线程池
        .subscribeOn(Schedulers.parallel())
        // 慢速消费者依然运行在 main 线程（因为没有 publishOn 切换）
        .subscribe(new MySubscriber());

        // 等待，以便观察异步线程的执行结果
        TimeUnit.SECONDS.sleep(2);
    }
}

