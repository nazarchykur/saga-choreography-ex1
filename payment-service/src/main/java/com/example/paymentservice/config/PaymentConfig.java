package com.example.paymentservice.config;

import com.example.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.example.event.order.OrderEvent;
import org.example.event.order.OrderStatus;
import org.example.event.payment.PaymentEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class PaymentConfig {

    private final PaymentService service;

    @Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor() {
        return flux -> flux.flatMap(this::processPayment);
    }

    private Mono<PaymentEvent> processPayment(OrderEvent event) {
        if (event.getOrderStatus().equals(OrderStatus.ORDER_CREATED)) {
            return Mono.fromSupplier(() -> service.newOrderEvent(event));
        } else {
            return Mono.fromRunnable(() -> service.cancelOrderEvent(event));
        }
    }
}

/*
    PaymentConfig sets up a bean for processing payment-related events using reactive programming with Project Reactor.
    Let's break down this code step by step:

        - paymentProcessor():
            - This method is annotated with @Bean, indicating that it defines a Spring bean.
            - It creates and returns a Function<Flux<OrderEvent>, Flux<PaymentEvent>>. This function takes a Flux<OrderEvent>
                as input and returns a Flux<PaymentEvent> as output.
            - It uses the flatMap operator to transform each incoming OrderEvent into a PaymentEvent using the processPayment method.

        - processPayment() Method:
            - This is a private method that processes each OrderEvent and returns a Mono<PaymentEvent>. Mono is a reactive
                type that represents a single item or an error. In this context, it's used to represent a single PaymentEvent.
            - Inside the method, it checks the OrderStatus of the incoming OrderEvent:
                - If the OrderStatus is ORDER_CREATED, it calls service.newOrderEvent(event) to create a new PaymentEvent
                    using the PaymentService. The Mono.fromSupplier method is used to wrap the result of the service call as a Mono.
                - If the OrderStatus is not ORDER_CREATED, it calls service.cancelOrderEvent(event) to cancel the order
                    event using the PaymentService. The Mono.fromRunnable method is used to wrap the cancellation action as a Mono.



    In summary, this PaymentConfig class defines a Spring bean named paymentProcessor, which is a Function that processes
    incoming OrderEvent objects and produces corresponding PaymentEvent objects. The actual processing logic is encapsulated
    in the private processPayment method, which uses the injected PaymentService to create or cancel payment events based
    on the order status. This approach leverages reactive programming to handle events asynchronously and efficiently.

 */