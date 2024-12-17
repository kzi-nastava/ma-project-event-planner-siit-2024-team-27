package com.wde.eventplanner.models.productBudgetItem;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductBudgetItemDTO {
    private UUID id;
    private Double maxPrice;
    private UUID productCategoryId;
    private UUID eventId;
    private UUID productId;
}
