package com.wde.eventplanner.models.event;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CalendarService {
    private String id;
    private Integer version;
    private String name;
    private ArrayList<CalendarEvent> events;
}