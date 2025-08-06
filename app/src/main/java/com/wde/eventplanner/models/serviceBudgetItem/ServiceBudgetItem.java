package com.wde.eventplanner.models.serviceBudgetItem;

import com.wde.eventplanner.models.event.ListingBudgetItemDTO;
import com.wde.eventplanner.models.listing.ListingType;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ServiceBudgetItem {
    private UUID id;
    private UUID eventId;
    private Double maxPrice;
    private UUID serviceCategoryId;
    private UUID serviceId;
    private Integer serviceVersion;

    public ListingBudgetItemDTO toListingBudgetItem() {
        return new ListingBudgetItemDTO(id, ListingType.SERVICE, maxPrice,
                serviceCategoryId == null ? null : serviceCategoryId.toString(),
                serviceId == null ? null : serviceId.toString(), serviceVersion);
    }
}
