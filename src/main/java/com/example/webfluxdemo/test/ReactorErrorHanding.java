package com.example.webfluxdemo.test;


import reactor.core.publisher.Flux;

/**
 * @Classname ReactorErrorHanding
 * @Description TODO
 * @Date 2025/6/21 19:51
 * @Author Wilson Chen
 */
public class ReactorErrorHanding {

    public static void main(String[] args) {
        //创建一个 Flux<String>，包含以下几个名字: "tony", "steve", "thanos"。
        Flux<String> flux = Flux.just("tony", "steve", "thanos");
        //使用 .map() 操作符来处理这个流。在 map 的逻辑中：
        //如果名字是 "thanos"，就主动抛出一个异常 new RuntimeException("The Mad Titan appears!")。
        //对于其他名字，将其转换为大写。
        Flux<String> fluxWithError = flux.map(name -> {
            if (name.equals("thanos")) {
                throw new RuntimeException("The Mad Titan appears!");
            }
            return name.toUpperCase();
        });
        Flux<String> avengersAssemble = fluxWithError.onErrorResume(e -> Flux.just("avengers assemble"));
        //在 .map() 之后，链接一个 .onErrorResume() 操作符。
        //在 .onErrorResume() 的逻辑中，捕获到异常后，返回一个备用的 Flux，这个备用流只包含一个字符串："avengers assemble"。
        //订阅最终的流，并将结果打印到控制台。
        avengersAssemble.subscribe(System.out::println);
        System.out.println("Done");
    }
}


