package com.wde.eventplanner.models.productBudgetItem;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BuyProductDTO {
    private UUID eventId;
    private UUID productBudgetItemId;
    private UUID productId;
}
