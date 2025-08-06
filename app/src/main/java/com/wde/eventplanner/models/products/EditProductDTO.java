package com.wde.eventplanner.models.products;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EditProductDTO {
    private UUID staticProductId;
    private Integer version;
    private UUID productCategoryId;
    private List<UUID> availableEventTypeIds;

    private String name;
    private Double salePercentage;
    private String description;
    private Boolean isPrivate;
    private Boolean isAvailable;
    private Boolean isActive;
    private Double price;
}
