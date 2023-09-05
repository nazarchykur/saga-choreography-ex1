package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.OrderInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderInventoryRepository extends JpaRepository<OrderInventory, Integer> {
}