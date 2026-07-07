package com.example.picturebackend.api.aliyunai.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    // 共用交换机
    public static final String AI_EXCHANGE = "ai.exchange";

    // 扩图队列
    public static final String OUT_PAINTING_QUEUE = "ai.outpainting.queue";
    public static final String OUT_PAINTING_KEY = "ai.outpainting.key";

    // 文生图队列
    public static final String TEXT_TO_IMAGE_QUEUE = "ai.text2image.queue";
    public static final String TEXT_TO_IMAGE_KEY = "ai.text2image.key";

    private Queue createPriorityQueue(String name) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 10);
        return new Queue(name, true, false, false, args);
    }

    @Bean
    public Queue outPaintingQueue() {
        return createPriorityQueue(OUT_PAINTING_QUEUE);
    }

    @Bean
    public Queue textToImageQueue() {
        return createPriorityQueue(TEXT_TO_IMAGE_QUEUE);
    }

    @Bean
    public DirectExchange aiExchange() {
        return new DirectExchange(AI_EXCHANGE, true, false);
    }

    @Bean
    public Binding outPaintingBinding() {
        return BindingBuilder.bind(outPaintingQueue())
                .to(aiExchange()).with(OUT_PAINTING_KEY);
    }

    @Bean
    public Binding textToImageBinding() {
        return BindingBuilder.bind(textToImageQueue())
                .to(aiExchange()).with(TEXT_TO_IMAGE_KEY);
    }
}
