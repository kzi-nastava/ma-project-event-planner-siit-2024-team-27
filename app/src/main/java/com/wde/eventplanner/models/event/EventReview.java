package com.wde.eventplanner.models.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventReview {
    private int grade;
    private String comment;
    private UUID eventId;
    private UUID guestId;
}
