package com.example.orderservice.config;

import com.example.orderservice.entity.PurchaseOrder;
import com.example.orderservice.repository.PurchaseOrderRepository;
import com.example.orderservice.service.OrderStatusPublisher;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.event.inventory.InventoryStatus;
import org.example.event.order.OrderStatus;
import org.example.event.payment.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class OrderStatusUpdateEventHandler {

    private final PurchaseOrderRepository repository;
    private final OrderStatusPublisher publisher;

    @Transactional
    public void updateOrder(final UUID id, Consumer<PurchaseOrder> consumer) {
        this.repository.findById(id)
                .ifPresent(consumer.andThen(this::updateOrder));

    }

    private void updateOrder(PurchaseOrder purchaseOrder) {
        if (Objects.isNull(purchaseOrder.getInventoryStatus()) || Objects.isNull(purchaseOrder.getPaymentStatus())) {
            return;
        }
        var isComplete = PaymentStatus.RESERVED.equals(purchaseOrder.getPaymentStatus()) && InventoryStatus.RESERVED.equals(purchaseOrder.getInventoryStatus());
        var orderStatus = isComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
        purchaseOrder.setOrderStatus(orderStatus);
        if (!isComplete) {
            this.publisher.raiseOrderEvent(purchaseOrder, orderStatus);
        }
    }

}

/*
    OrderStatusUpdateEventHandler. This service is responsible for updating the status of purchase orders based on events
    related to payments and inventory. Let's break down the code and understand it in detail:


    updateOrder Method:
        - This method is marked with @Transactional, which indicates that it's a transactional method, and it will be
            executed within a transactional context. If something goes wrong during the method execution, the transaction
            can be rolled back.

        It takes two parameters:
            - UUID id: The identifier of the purchase order to be updated.
            - Consumer<PurchaseOrder> consumer: A Consumer functional interface that takes a PurchaseOrder as input.
                This parameter allows for custom update logic to be passed as a lambda function.
            - Inside the method:
                - It uses the PurchaseOrderRepository to find a purchase order by its id.
                - If a purchase order with the specified id is found, it applies the provided consumer function to it.
                - The andThen method is used to chain the consumer function with the updateOrder method, which will apply
                    additional updates to the purchase order if necessary.


    updateOrder (Private) Method:
        - This method is responsible for updating the purchase order based on the payment and inventory status.
            - It first checks if either the payment status or inventory status is null, and if so, it returns without making
                any changes.
            - If both payment and inventory statuses are not null, it checks whether the payment status is "RESERVED" and
                the inventory status is "RESERVED." If both conditions are met, it sets the order status to "ORDER_COMPLETED";
                otherwise, it sets the order status to "ORDER_CANCELLED."
            - If the order status is set to "ORDER_CANCELLED," it also calls the raiseOrderEvent method on the
                OrderStatusPublisher to notify other parts of the application about the order cancellation.



    In summary:
        - The OrderStatusUpdateEventHandler service is responsible for updating purchase orders' statuses based on payment
            and inventory events.

        - The updateOrder method allows for custom update logic to be applied to purchase orders, and it ensures that the
            updates are performed within a transactional context.

        - The updateOrder method defines the business logic for determining the order status based on payment and inventory statuses.

        - It also raises order-related events if the order status is "ORDER_CANCELLED."

        - This class encapsulates the logic for handling order status updates in a modular and transactional manner,
            making it suitable for use in a Spring-based application.

 */