package com.wde.eventplanner.models.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventActivitiesDTO {
    private List<AgendaItem> eventActivities;
}
