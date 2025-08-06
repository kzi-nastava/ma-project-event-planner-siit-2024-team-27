package com.wde.eventplanner.models.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventDetailedDTO {
    private UUID id;
    private String name;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String city;
    private String address;
    private Double averageRating;
    private List<String> images;
    private String organizerCredentials;
    private Boolean isFavorite;
    private boolean isAccepted;
    private UUID organizerId;
    private UUID organizerProfileId;
}
