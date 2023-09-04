package com.example.orderservice.service;

import com.example.orderservice.entity.PurchaseOrder;
import com.example.orderservice.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public List<PurchaseOrder> getAll() {
        return purchaseOrderRepository.findAll();
    }
}
