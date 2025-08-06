package com.wde.eventplanner.models.productBudgetItem;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BuyProductDTO {
    private UUID eventId;
    private UUID productId;
    private UUID productBudgetItemId;

    public BuyProductDTO(UUID eventId, UUID productId) {
        this.eventId = eventId;
        this.productId = productId;
        this.productBudgetItemId = null;
    }
}
