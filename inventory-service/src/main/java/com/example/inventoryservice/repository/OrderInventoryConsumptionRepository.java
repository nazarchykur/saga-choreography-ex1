package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.OrderInventoryConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderInventoryConsumptionRepository extends JpaRepository<OrderInventoryConsumption, UUID> {
}