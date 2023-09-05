package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.OrderInventoryConsumption;
import com.example.inventoryservice.repository.OrderInventoryConsumptionRepository;
import com.example.inventoryservice.repository.OrderInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.example.dto.InventoryDto;
import org.example.event.inventory.InventoryEvent;
import org.example.event.inventory.InventoryStatus;
import org.example.event.order.OrderEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final OrderInventoryRepository inventoryRepository;
    private final OrderInventoryConsumptionRepository consumptionRepository;

    @Transactional
    public InventoryEvent newOrderInventory(OrderEvent orderEvent) {
        InventoryDto dto = InventoryDto.of(orderEvent.getPurchaseOrder().getOrderId(), orderEvent.getPurchaseOrder().getProductId());

        return inventoryRepository.findById(orderEvent.getPurchaseOrder().getProductId())
                .filter(orderInventory -> orderInventory.getAvailableInventory() > 0)
                .map(orderInventory -> {
                    orderInventory.setAvailableInventory(orderInventory.getAvailableInventory() - 1);
                    consumptionRepository.save(OrderInventoryConsumption.of(orderEvent.getPurchaseOrder().getOrderId(), orderEvent.getPurchaseOrder().getProductId(), 1));
                    return new InventoryEvent(dto, InventoryStatus.RESERVED);
                })
                .orElse(new InventoryEvent(dto, InventoryStatus.REJECTED));
    }

    @Transactional
    public void cancelOrderInventory(OrderEvent orderEvent) {
        consumptionRepository.findById(orderEvent.getPurchaseOrder().getOrderId())
                .ifPresent(orderInventoryConsumption -> {
                    inventoryRepository.findById(orderInventoryConsumption.getProductId())
                            .ifPresent(orderInventory ->
                                    orderInventory.setAvailableInventory(orderInventory.getAvailableInventory() + orderInventoryConsumption.getQuantityConsumed())
                            );
                    consumptionRepository.delete(orderInventoryConsumption);
                });
    }

}

/*
    InventoryService class that handles operations related to inventory management. Let's go through the code step by step:

        - @Transactional: This annotation indicates that the methods newOrderInventory and cancelOrderInventory are transactional.
            Transactions ensure that a series of database operations are treated as a single unit of work, and they are either
            all completed successfully or none of them are.

        - public InventoryEvent newOrderInventory(OrderEvent orderEvent):
            - This method is used to handle the inventory allocation process when a new order is created.
            - It first creates an InventoryDto object using information from the orderEvent, such as the orderId and productId.
            - It attempts to find an OrderInventory entity by productId using inventoryRepository.findById. If an OrderInventory
                with the given productId exists and has available inventory (indicated by orderInventory.getAvailableInventory() > 0),
                it performs the following actions:
                    - Decrements the available inventory count by 1 (orderInventory.setAvailableInventory(orderInventory.getAvailableInventory() - 1)).
                    - Saves a new OrderInventoryConsumption record, indicating that one unit of the product has been consumed.
                    - Returns an InventoryEvent with InventoryStatus.RESERVED.

            - If no matching OrderInventory is found or the available inventory is zero, it returns an InventoryEvent with InventoryStatus.REJECTED.


        - public void cancelOrderInventory(OrderEvent orderEvent):
            This method handles the cancellation of an order and the corresponding inventory adjustments.

                - It attempts to find an OrderInventoryConsumption entity by orderId using consumptionRepository.findById.
                    If it finds a consumption record, it proceeds with the following actions:
                    - Retrieves the corresponding OrderInventory by productId using inventoryRepository.findById.
                    - Increases the available inventory count by the quantity that was previously consumed
                        (orderInventory.setAvailableInventory(orderInventory.getAvailableInventory() + orderInventoryConsumption.getQuantityConsumed())).
                    - Deletes the OrderInventoryConsumption record, effectively undoing the consumption.



    In summary this InventoryService class encapsulates the logic for managing inventory related to order processing.
    It uses Spring's @Transactional annotation to ensure data consistency during inventory updates, and it interacts with
    repositories to access and modify inventory data in a database or other data store.

 */
