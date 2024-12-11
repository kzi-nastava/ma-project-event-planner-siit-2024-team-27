package com.wde.eventplanner.models.productBudgetItem;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProductBudgetItemDTO {
    private UUID id;
    private Double maxPrice;
    private UUID productCategoryId;
    private UUID eventId;
    private UUID productId;
}
