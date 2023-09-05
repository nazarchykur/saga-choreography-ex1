package com.example.paymentservice.repository;

import com.example.paymentservice.entity.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserTransactionRepository extends JpaRepository<UserTransaction, UUID> {
}