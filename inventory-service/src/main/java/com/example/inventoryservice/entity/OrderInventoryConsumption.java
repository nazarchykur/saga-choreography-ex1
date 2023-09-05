package com.example.inventoryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class OrderInventoryConsumption {

    @Id
    private UUID orderId;
    private int productId;
    private int quantityConsumed;

}
