package com.wde.eventplanner.models.serviceBudgetItem;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class ReserveService {
    private UUID eventId;
    private UUID serviceId;
    private String startTime;
    private String endTime;
}
