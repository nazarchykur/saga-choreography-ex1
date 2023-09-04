package com.example.orderservice.service;

import com.example.orderservice.entity.PurchaseOrder;
import lombok.RequiredArgsConstructor;
import org.example.dto.PurchaseOrderDto;
import org.example.event.order.OrderEvent;
import org.example.event.order.OrderStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
public class OrderStatusPublisher {

    private final Sinks.Many<OrderEvent> orderSink;

    public void raiseOrderEvent(final PurchaseOrder purchaseOrder, OrderStatus orderStatus) {
        var dto = PurchaseOrderDto.of(
                purchaseOrder.getId(),
                purchaseOrder.getProductId(),
                purchaseOrder.getPrice(),
                purchaseOrder.getUserId()
        );
        var orderEvent = new OrderEvent(dto, orderStatus);
        orderSink.tryEmitNext(orderEvent);
    }
}