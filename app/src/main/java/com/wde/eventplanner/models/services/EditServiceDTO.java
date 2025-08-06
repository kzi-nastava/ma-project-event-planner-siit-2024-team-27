package com.wde.eventplanner.models.services;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EditServiceDTO {
    private UUID staticServiceId;
    private Integer version;
    private UUID serviceCategoryId;
    private List<UUID> availableEventTypeIds;

    private String name;
    private Double salePercentage;
    private String description;
    private Boolean isPrivate;
    private Boolean isAvailable;
    private Integer cancellationDeadline;
    private Integer reservationDeadline;
    private Boolean isActive;
    private Boolean isConfirmationManual;
    private Double price;
    private Integer minimumDuration;
    private Integer maximumDuration;
}
