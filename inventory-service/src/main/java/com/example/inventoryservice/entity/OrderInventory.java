package com.example.inventoryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class OrderInventory {

    @Id
    private Integer productId;
    private Integer availableInventory;

}
