package com.wde.eventplanner.models.productBudgetItem;

import com.wde.eventplanner.models.event.ListingBudgetItemDTO;
import com.wde.eventplanner.models.listing.ListingType;

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
    private UUID eventId;
    private Double maxPrice;
    private UUID productCategoryId;
    private UUID productId;
    private Integer productVersion;

    public ListingBudgetItemDTO toListingBudgetItem() {
        return new ListingBudgetItemDTO(id, ListingType.PRODUCT, maxPrice,
                productCategoryId == null ? null : productCategoryId.toString(),
                productId == null ? null : productId.toString(), productVersion);
    }
}
