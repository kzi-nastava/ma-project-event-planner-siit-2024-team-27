package com.wde.eventplanner.models.products;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Product {
    private UUID staticProductId;
    private Integer version;
    private String name;
    private String description;
    private Double price;
    //    private List<String> images // todo image service
    private Double oldPrice;
    private Boolean isActive;
    private Boolean isAvailable;
    private Boolean isPrivate;
    private UUID productCategoryId;
    private List<UUID> availableEventTypesIds;
    private Double rating;
}
