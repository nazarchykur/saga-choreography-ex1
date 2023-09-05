package com.example.paymentservice.service;

import com.example.paymentservice.entity.UserTransaction;
import com.example.paymentservice.repository.UserBalanceRepository;
import com.example.paymentservice.repository.UserTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.example.dto.PaymentDto;
import org.example.event.order.OrderEvent;
import org.example.event.payment.PaymentEvent;
import org.example.event.payment.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserBalanceRepository balanceRepository;
    private final UserTransactionRepository transactionRepository;

    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        var purchaseOrder = orderEvent.getPurchaseOrder();
        var dto = PaymentDto.of(purchaseOrder.getOrderId(), purchaseOrder.getUserId(), purchaseOrder.getPrice());
        return balanceRepository.findById(purchaseOrder.getUserId())
                .filter(userBalance -> userBalance.getBalance() >= purchaseOrder.getPrice())
                .map(userBalance -> {
                    userBalance.setBalance(userBalance.getBalance() - purchaseOrder.getPrice());
                    transactionRepository.save(UserTransaction.of(purchaseOrder.getOrderId(), purchaseOrder.getUserId(), purchaseOrder.getPrice()));
                    return new PaymentEvent(dto, PaymentStatus.RESERVED);
                })
                .orElse(new PaymentEvent(dto, PaymentStatus.REJECTED));
    }

    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {
        this.transactionRepository.findById(orderEvent.getPurchaseOrder().getOrderId())
                .ifPresent(userTransaction -> {
                    transactionRepository.delete(userTransaction);
                    balanceRepository.findById(userTransaction.getUserId())
                            .ifPresent(userBalance -> userBalance.setBalance(userBalance.getBalance() + userTransaction.getAmount()));
                });
    }
}

/*
    The PaymentService class appears to be a Spring service responsible for handling payment-related operations based on
    incoming OrderEvent objects. Let's break down this class and its methods:
        - @Service Annotation:
            This annotation marks the class as a Spring service, indicating that it contains business logic and should be
            managed by the Spring container.

        - @RequiredArgsConstructor Annotation:
            This annotation generates a constructor that includes all required fields as parameters. In this case, it
            generates a constructor that injects two dependencies: UserBalanceRepository and UserTransactionRepository.


        - newOrderEvent(OrderEvent orderEvent) Method:
            - This method is annotated with @Transactional, indicating that it should be executed within a transactional
                context. If any exceptions occur during the execution of this method, the transaction can be rolled back.
            - It first extracts the PurchaseOrder from the OrderEvent and creates a PaymentDto based on the purchase order's data.
            - It then attempts to find the user's balance using the balanceRepository. If a user's balance is found and
                it's sufficient to cover the purchase order's price, it performs the following actions within a transaction:
                    - It deducts the purchase order's price from the user's balance.
                    - It saves a new UserTransaction record to record the payment.
                    - It returns a new PaymentEvent with a PaymentStatus of RESERVED.

            - If the user's balance is not sufficient or the user's balance cannot be found, it returns a PaymentEvent
                with a PaymentStatus of REJECTED.


        - cancelOrderEvent(OrderEvent orderEvent) Method:
            - This method is also annotated with @Transactional, indicating that it should be executed within a transactional context.
            - This method handles the cancellation of a payment event based on the incoming OrderEvent.
            - It attempts to find a UserTransaction record corresponding to the purchase order in the transactionRepository.
                If a transaction is found, it performs the following actions within a transaction:
                    - It deletes the transaction record.
                    - It retrieves the user's balance using the balanceRepository.
                    - If the user's balance is found, it adds back the transaction amount to the user's balance.


    In summary, the PaymentService class encapsulates the logic for handling payment-related operations based on incoming
    OrderEvent objects. It ensures that transactions are used to maintain data consistency when modifying the user's balance
    and recording transactions. Depending on the scenario, it creates payment events with different statuses (RESERVED or REJECTED)
    and handles the cancellation of transactions when necessary.

 */