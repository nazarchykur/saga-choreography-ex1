package org.example.event.inventory;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.InventoryDto;
import org.example.event.Event;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class InventoryEvent implements Event {

    private final UUID eventId = UUID.randomUUID();
    private final Date date = new Date();
    private InventoryDto inventory;
    private InventoryStatus inventoryStatus;

    public InventoryEvent(InventoryDto inventory, InventoryStatus inventoryStatus) {
        this.inventory = inventory;
        this.inventoryStatus = inventoryStatus;
    }

    @Override
    public UUID getEventId() {
        return this.eventId;
    }

    @Override
    public Date getDate() {
        return this.date;
    }
}
