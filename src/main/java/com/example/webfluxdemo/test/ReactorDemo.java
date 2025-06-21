package com.example.webfluxdemo.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname ReactorDemo
 * @Description Reactor 核心类型演示
 * @Date 2025/6/19 22:14
 * @Author Wilson Chen
 */
public class ReactorDemo {

    public static void main(String[] args) {
        System.out.println("--- Flux 示例 ---");
        demonstrateFlux();

        System.out.println("\n--- Mono 示例 ---");
        demonstrateMono();

        System.out.println("\n--- 完成你的专属任务 ---");
        completeAssignedTask();

        System.out.println("\n--- 核心操作符串联任务 ---");
        demonstrateOperators();
    }

    /**
     * 演示 Flux 的创建与订阅
     */
    public static void demonstrateFlux() {
        // 示例1: 从一组明确的元素创建
        // 我们在这里加入 .log() 操作符，来观察内部发生了什么
        System.out.println("示例1: Flux.just() + .log()");
        Flux<String> flux1 = Flux.just("Apple", "Banana", "Cherry")
                .log(); // 添加 log() 来观察事件

        System.out.println(">>> flux1 已声明，但未订阅，无任何事件发生。");
        // **关键步骤：订阅 Flux 以触发数据流**
        flux1.subscribe(fruit -> System.out.println("消费到水果: " + fruit));
        System.out.println(">>> flux1 订阅完成。");


        System.out.println("\n示例2: Flux.fromIterable()");
        List<String> fruits = Arrays.asList("Grape", "Orange", "Strawberry");
        Flux<String> flux2 = Flux.fromIterable(fruits);
        flux2.subscribe(System.out::println); // 使用方法引用简化代码


        System.out.println("\n示例3: Flux.range()");
        Flux<Integer> flux3 = Flux.range(1, 5); // 将发出 1, 2, 3, 4, 5
        flux3.subscribe(number -> System.out.print(number + " "));
        System.out.println(); // 换行
    }

    /**
     * 演示 Mono 的创建与订阅
     */
    public static void demonstrateMono() {
        // 示例1: 从一个明确的元素创建
        System.out.println("示例1: Mono.just()");
        Mono<String> mono1 = Mono.just("OnePlus");
        mono1.subscribe(brand -> System.out.println("手机品牌: " + brand));


        // 示例2: 从一个可能为 null 的对象创建
        System.out.println("\n示例2: Mono.justOrEmpty() - 非空情况");
        Mono<String> mono2_nonEmpty = Mono.justOrEmpty("Xiaomi");
        mono2_nonEmpty.subscribe(brand -> System.out.println("手机品牌: " + brand));

        System.out.println("\n示例3: Mono.justOrEmpty() - 为空情况");
        Mono<String> mono2_empty = Mono.justOrEmpty(null); // 这是一个空的 Mono
        // 对于空的流，subscribe 中的消费逻辑不会执行，但完成信号(onComplete)会触发
        mono2_empty.subscribe(
                brand -> System.out.println("这部手机是: " + brand), // onNext - 不会执行
                error -> System.err.println("出错了: " + error),   // onError - 不会执行
                () -> System.out.println("流处理完成，没有手机。")  // onComplete - 会执行
        );
    }

    /**
     * 完成之前布置的编码任务
     */
    public static void completeAssignedTask() {
        // 任务1: 创建一个 Flux，包含你的三个喜欢的电影名称
        System.out.println("任务1: 喜欢的电影");
        Flux<String> favoriteMovies = Flux.just("Inception", "The Dark Knight", "Interstellar");
        favoriteMovies.subscribe(movie -> System.out.println("- " + movie));

        // 任务2: 创建一个 Mono，包含你的名字 (从你的 Javadoc 获取)
        System.out.println("\n任务2: 你的名字");
        Mono<String> myName = Mono.just("Wilson Chen");
        myName.subscribe(name -> System.out.println("作者是: " + name));

        // 任务3: 创建一个 Flux，包含从 10 到 20 的所有整数
        System.out.println("\n任务3: 10到20的数字");
        Flux<Integer> numbers = Flux.range(10, 11); // range(start, count)，所以是(10, 11)
        numbers.map(n -> n + " ")
                .subscribe(System.out::print);
        System.out.println();
    }

    /**
     * 演示 filter, map, flatMap 操作符的串联使用
     */
    public static void demonstrateOperators() {
        // 1. 创建源 Flux
        Flux.just("peter", "bruce", "steve", "tony")
                // 在这里 .log() 可以清晰地看到每个阶段的数据变化
                .log()

                // 2. 使用 filter，只保留长度大于4的名字
                // 输入: "peter", "bruce", "steve", "tony"
                // 输出: "peter", "bruce", "steve"
                .filter(name -> name.length() > 4)

                // 3. 使用 map，将名字转换为大写
                // 输入: "peter", "bruce", "steve"
                // 输出: "PETER", "BRUCE", "STEVE"
                .map(String::toUpperCase)

                // 4. 使用 flatMap，为每个名字执行一个异步操作
                // 输入: "PETER", "BRUCE", "STEVE"
                // 输出: "PETER[HERO]", "BRUCE[HERO]", "STEVE[HERO]"
                .flatMap(ReactorDemo::decorateName)

                // 5. 订阅并打印最终结果
                .subscribe(System.out::println);
    }

    /**
     * 一个模拟的异步装饰方法
     * @param name 名字
     * @return 返回一个 Mono，它将在100毫秒后发出一个带后缀的名字
     */
    private static Mono<String> decorateName(String name) {
        return Mono.just(name + "[HERO]")
                .delayElement(Duration.ofMillis(100));
    }
}