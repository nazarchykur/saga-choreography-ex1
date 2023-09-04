package com.example.orderservice.config;

import lombok.RequiredArgsConstructor;
import org.example.event.inventory.InventoryEvent;
import org.example.event.payment.PaymentEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class EventHandlersConfig {

    private final OrderStatusUpdateEventHandler orderEventHandler;

    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer() {
        return paymentEvent -> {
            orderEventHandler.updateOrder(paymentEvent.getPayment().getOrderId(), purchaseOrder -> {
                purchaseOrder.setPaymentStatus(paymentEvent.getPaymentStatus());
            });
        };
    }

    @Bean
    public Consumer<InventoryEvent> inventoryEventConsumer() {
        return inventoryEvent -> {
            orderEventHandler.updateOrder(inventoryEvent.getInventory().getOrderId(), purchaseOrder -> {
                purchaseOrder.setInventoryStatus(inventoryEvent.getInventoryStatus());
            });
        };
    }
}

/*
    class EventHandlersConfig that defines two beans, paymentEventConsumer and inventoryEventConsumer, which are consumers
    for handling different types of events (PaymentEvent and InventoryEvent).
    Let's break down the code and understand these methods in detail:

    - paymentEventConsumer() Method:
        This method is annotated with @Bean, indicating that it defines a Spring bean of type Consumer<PaymentEvent>.
        It's responsible for handling PaymentEvent objects.
        Inside the method, there's a lambda expression that defines the behavior of the consumer.
        When a PaymentEvent is received, it triggers an update to the order status using the orderEventHandler bean.
        The updateOrder method is called on the orderEventHandler bean, and it passes the orderId and a lambda function
        that updates the payment status of the associated purchase order.

    - inventoryEventConsumer() Method:
        Similar to paymentEventConsumer(), this method is annotated with @Bean, indicating that it defines a Spring bean
        of type Consumer<InventoryEvent>. It's responsible for handling InventoryEvent objects.
        Inside the method, there's a lambda expression that defines the behavior of the consumer.
        When an InventoryEvent is received, it triggers an update to the order status using the orderEventHandler bean.
        The updateOrder method is called on the orderEventHandler bean, and it passes the orderId and a lambda function
        that updates the inventory status of the associated purchase order.



    In summary:
        paymentEventConsumer and inventoryEventConsumer are Spring beans of type Consumer that handle PaymentEvent and
        InventoryEvent objects, respectively.
        They both use the orderEventHandler bean to update the order status based on the information provided in the incoming events.
 */
