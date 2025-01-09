package com.wde.eventplanner.models.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventAdminDTO {
    private UUID id;
    private String name;
    private String city;
    private Integer attendance;
    private Double rating;
}
