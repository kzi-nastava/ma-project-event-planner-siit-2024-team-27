package com.wde.eventplanner.models.event;

import com.wde.eventplanner.models.listing.Listing;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarEvent {
    private String id;
    private String name;
    private LocalDate date;
    private LocalTime time;
    private Double rating;
    private String location;
    private ArrayList<AgendaItem> activites;
    private ArrayList<Listing> listings;
    private ArrayList<String> images;
}
