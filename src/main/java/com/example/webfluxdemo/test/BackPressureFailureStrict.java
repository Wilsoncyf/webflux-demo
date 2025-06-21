package com.example.webfluxdemo.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import java.util.stream.IntStream;

public class BackPressureFailureStrict {
    public static void main(String[] args) {
        System.out.println("--- 演示 MissingBackpressureException (严格模式) ---");

        Flux.create(emitter -> {
                    IntStream.range(1, 1000).forEach(i -> {
                        if (emitter.isCancelled()) {
                            return;
                        }
                        System.out.println("上游正在推送: " + i);
                        emitter.next(i);
                    });
                    emitter.complete();
                }, FluxSink.OverflowStrategy.ERROR) // **明确指定溢出策略为 ERROR**
                .log()
                .subscribe(new MySubscriber());
    }
}