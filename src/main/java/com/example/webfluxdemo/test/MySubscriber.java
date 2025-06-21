package com.example.webfluxdemo.test;


import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

/**
 * @Classname MySubscriber
 * @Description TODO
 * @Date 2025/6/21 11:57
 * @Author Wilson Chen
 */
public class MySubscriber extends BaseSubscriber<Object> {

    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        System.out.println("Subscribed");
        request(1);
    }

    @Override
    protected void hookOnNext(Object value) {
        System.out.println(Thread.currentThread().getName() + " Received: " + value);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        request(1);
    }

}
