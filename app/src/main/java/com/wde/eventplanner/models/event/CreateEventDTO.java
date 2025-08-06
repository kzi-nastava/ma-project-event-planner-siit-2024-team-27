package com.wde.eventplanner.models.event;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateEventDTO {
    private UUID eventId;
    private String name;
    private String description;
    private String city;
    private String address;
    private Boolean isPublic;
    private String date;
    private String time;
    private Integer guestCount;
    private String eventTypeId;
    private List<UUID> agenda;

    private UUID organizerProfileId;

    private Integer latitude;
    private Integer longitude;
}
