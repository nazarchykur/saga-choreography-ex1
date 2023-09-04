package com.example.orderservice.config;

import org.example.event.order.OrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class OrderConfig {

    @Bean
    public Sinks.Many<OrderEvent> orderSink() {
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<OrderEvent>> orderSupplier(Sinks.Many<OrderEvent> sink) {
        return sink::asFlux;
    }

}

/*
    @Bean
    public Sinks.Many<OrderEvent> orderSink() {
        return Sinks.many().unicast().onBackpressureBuffer();
    }
    This method is annotated with @Bean, indicating that it defines a Spring bean of type Sinks.Many<OrderEvent>.
    Let's break down what each part of this method does:

        - Sinks.many(): This creates an instance of a Project Reactor Sinks.Many sink. Sinks is a part of Project Reactor
            and provides various types of sinks for creating reactive streams. Sinks.Many is used when you want to emit
            multiple elements to multiple subscribers.

        - unicast(): This method configures the sink to operate in a unicast mode, meaning each subscriber will receive
            its own copy of the data. This is suitable when you want to ensure that each subscriber processes data
            independently without sharing it with others.

        - onBackpressureBuffer(): This method configures the sink to handle backpressure by buffering elements if subscribers
            cannot keep up with the rate of emissions. Backpressure occurs when publishers emit data faster than subscribers
            can consume it, and this method helps prevent data loss by buffering excess data.


    @Bean
    public Supplier<Flux<OrderEvent>> orderSupplier(Sinks.Many<OrderEvent> sink) {
        return sink::asFlux;
    }

    This method is also annotated with @Bean and defines a Spring bean of type Supplier<Flux<OrderEvent>>.
    It takes a parameter, Sinks.Many<OrderEvent> sink, which is the same sink created in the orderSink method.
    Let's break down what this method does:

        - Supplier<Flux<OrderEvent>>: This defines a supplier that will provide a Flux<OrderEvent>. A Flux is a reactive
        stream that emits a sequence of elements asynchronously.

        - sink::asFlux: This is a method reference that converts the Sinks.Many<OrderEvent> (sink) into a Flux<OrderEvent>.
            In other words, it creates a reactive stream (Flux) that emits elements from the Sinks.Many sink.


    So, in summary:
        - orderSink creates a sink for emitting OrderEvent elements in a reactive manner, with buffering in case of
            backpressure. This sink can be used to publish events.

        - orderSupplier is a supplier that provides a Flux<OrderEvent> by converting the orderSink into a reactive stream.
            This can be used to subscribe and consume events from the orderSink.

    These beans are useful for building a reactive event handling system, allowing you to publish and consume OrderEvent
    data in an asynchronous and backpressure-aware way.

 */
