package com.example.paymentservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class UserTransaction {

    @Id
    private UUID orderId;
    private int userId;
    private int amount;

}
