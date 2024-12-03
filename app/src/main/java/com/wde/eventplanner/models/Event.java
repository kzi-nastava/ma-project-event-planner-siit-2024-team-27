package com.wde.eventplanner.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Event {
    private String title;
    private String time;
    private String date;
    private String location;
    private float rating;
}
