package com.wde.eventplanner.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventType {
    private String id;

    private String name;
    private String description;
    private Boolean isActive;
}
