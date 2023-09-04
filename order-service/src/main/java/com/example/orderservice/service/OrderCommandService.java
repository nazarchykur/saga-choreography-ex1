package com.example.orderservice.service;

import com.example.orderservice.entity.PurchaseOrder;
import com.example.orderservice.repository.PurchaseOrderRepository;
import lombok.AllArgsConstructor;
import org.example.dto.OrderRequestDto;
import org.example.event.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@AllArgsConstructor
public class OrderCommandService {

    private Map<Integer, Integer> productPriceMap;
    private PurchaseOrderRepository purchaseOrderRepository;
    private OrderStatusPublisher publisher;

    @Transactional
    public PurchaseOrder createOrder(OrderRequestDto orderRequestDTO){
        PurchaseOrder purchaseOrder = purchaseOrderRepository.save(dtoToEntity(orderRequestDTO));
        publisher.raiseOrderEvent(purchaseOrder, OrderStatus.ORDER_CREATED);

        return purchaseOrder;
    }

    private PurchaseOrder dtoToEntity(final OrderRequestDto dto){
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(dto.getOrderId());
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(productPriceMap.get(purchaseOrder.getProductId()));

        return purchaseOrder;
    }
}
