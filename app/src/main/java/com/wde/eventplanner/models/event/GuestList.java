package com.wde.eventplanner.models.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GuestList {
    private List<String> emails;
    private String eventId;
    private String organizerName;
    private String organizerSurname;
}