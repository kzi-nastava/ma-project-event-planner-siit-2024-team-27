package com.wde.eventplanner.models.event;

import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.productBudgetItem.ProductBudgetItemDTO;
import com.wde.eventplanner.models.serviceBudgetItem.ServiceBudgetItem;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ListingBudgetItemDTO {
    private UUID id;
    private ListingType listingType;
    private Double maxPrice;
    private String listingCategoryId;
    private String listingId;
    private Integer listingVersion;

    public ListingBudgetItemDTO(ListingBudgetItemDTO other) {
        this.id = other.id;
        this.listingType = other.listingType;
        this.maxPrice = other.maxPrice;
        this.listingCategoryId = other.listingCategoryId;
        this.listingId = other.listingId;
        this.listingVersion = other.listingVersion;
    }

    public ServiceBudgetItem toServiceBudgetItem(UUID eventId) {
        return new ServiceBudgetItem(id, eventId, maxPrice,
                listingCategoryId == null ? null : UUID.fromString(listingCategoryId),
                listingId == null ? null : UUID.fromString(listingId), listingVersion);
    }

    public ProductBudgetItemDTO toProductBudgetItem(UUID eventId) {
        return new ProductBudgetItemDTO(id, eventId, maxPrice,
                listingCategoryId == null ? null : UUID.fromString(listingCategoryId),
                listingId == null ? null : UUID.fromString(listingId), listingVersion);
    }
}
