package com.wde.eventplanner.models.event;

import com.wde.eventplanner.models.productBudgetItem.ProductBudgetItemDTO;
import com.wde.eventplanner.models.serviceBudgetItem.ServiceBudgetItem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventComplexView {
    private UUID id;
    private String name;
    private String description;
    private UUID eventTypeId;
    private LocalDate date;
    private LocalTime time;
    private String city;
    private String address;
    private Integer guestCount;
    private Boolean isPublic;
    private Double rating;
    private Double longitude;
    private Double latitude;
    private List<ProductBudgetItemDTO> productBudgetItems;
    private List<ServiceBudgetItem> serviceBudgetItems;
    private List<String> images;
}
