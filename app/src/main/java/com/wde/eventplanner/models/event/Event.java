package com.wde.eventplanner.models.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Event {
    private String name;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String city;
    private Double rating;
    private List<String> images;
}
