package com.wde.eventplanner.models;

public class Event {
    private String title;
    private String time;
    private String date;
    private String location;
    private float rating;

    public Event(String title, String time, String date, String location, float rating) {
        this.title = title;
        this.time = time;
        this.date = date;
        this.location = location;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public float getRating() {
        return rating;
    }
}
