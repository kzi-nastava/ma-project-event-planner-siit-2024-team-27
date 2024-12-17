package com.wde.eventplanner.models.services;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Service {
    private UUID staticServiceId;
    private Integer version;
    private UUID serviceCategoryId;
    private String name;
    private String description;
    private Double price;
    private Double oldPrice;
    //    private List<String> images // todo image service
    private Boolean isActive;
    private Boolean isAvailable;
    private Boolean isPrivate;
    private Boolean isConfirmationManual;
    private Integer duration;
    private Integer cancellationDeadLine;
    private Integer reservationDeadLine;
    private List<UUID> availableEventTypeIds;
    private Double rating;
}
