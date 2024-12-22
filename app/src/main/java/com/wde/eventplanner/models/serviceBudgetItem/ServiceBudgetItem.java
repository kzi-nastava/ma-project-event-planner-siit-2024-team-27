package com.wde.eventplanner.models.serviceBudgetItem;

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
    private Double maxPrice;
    private UUID serviceCategoryId;
    private UUID eventId;
    private UUID serviceId;
}
