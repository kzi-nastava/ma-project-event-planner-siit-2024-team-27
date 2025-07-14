package com.wde.eventplanner.models.event;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventActivitiesDTO {
    private List<AgendaItem> eventActivities;
    private UUID eventId;
}
