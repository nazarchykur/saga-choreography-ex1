package com.example.inventoryservice.config;

import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.example.event.inventory.InventoryEvent;
import org.example.event.order.OrderEvent;
import org.example.event.order.OrderStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class InventoryConfig {

    private final InventoryService inventoryService;

    @Bean
    public Function<Flux<OrderEvent>, Flux<InventoryEvent>> inventoryProcessor() {
        return flux -> flux.flatMap(this::processInventory);
    }

    private Mono<InventoryEvent> processInventory(OrderEvent event) {
        if (event.getOrderStatus().equals(OrderStatus.ORDER_CREATED)) {
            return Mono.fromSupplier(() -> inventoryService.newOrderInventory(event));
        }
        return Mono.fromRunnable(() -> inventoryService.cancelOrderInventory(event));
    }

}

/*
    It's a configuration class called InventoryConfig that defines a Spring Bean responsible for processing events related
    to inventory based on incoming order events. Let's break down the code step by step:

        - public Function<Flux<OrderEvent>, Flux<InventoryEvent>> inventoryProcessor():
            This method defines a Spring Bean named inventoryProcessor. It returns a Function that takes a Flux of OrderEvent
            as input and produces a Flux of InventoryEvent as output. In other words, it's a function that processes order
            events and produces inventory events.

        - flux -> flux.flatMap(this::processInventory):
            This is a lambda expression that defines the behavior of the inventoryProcessor function. It takes the incoming
            Flux of OrderEvent, and for each order event in the flux, it invokes the processInventory method. flatMap is used
            to transform and flatten the resulting Mono<InventoryEvent> for each order event into a Flux<InventoryEvent>.

        - private Mono<InventoryEvent> processInventory(OrderEvent event):
            This is a private method that processes an individual OrderEvent and returns a Mono<InventoryEvent>.
            Depending on the OrderStatus of the incoming event, it performs different actions:

            - If the OrderStatus is ORDER_CREATED, it uses Mono.fromSupplier to asynchronously invoke
                inventoryService.newOrderInventory(event) and return the result as a Mono. This means it's expecting a supplier
                function that will produce the InventoryEvent when called.

            - If the OrderStatus is not ORDER_CREATED, it uses Mono.fromRunnable to asynchronously invoke
                inventoryService.cancelOrderInventory(event). This method doesn't produce a value but is used for side-effect
                operations, such as canceling order inventory.



    In summary, this Spring configuration class defines a inventoryProcessor bean that processes order events and produces
    inventory events based on the order's status. The InventoryService dependency is injected via constructor injection,
    and the actual processing logic is implemented in the processInventory method. The @Bean annotation ensures that the
    inventoryProcessor function is registered as a Spring Bean and can be used throughout the application context.

 */
