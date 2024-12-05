package com.wde.eventplanner.models;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Event {
    private String name;
    private String description;
    private LocalDateTime date;
    private String city;
    private Double rating;
    private List<String> images;
}
