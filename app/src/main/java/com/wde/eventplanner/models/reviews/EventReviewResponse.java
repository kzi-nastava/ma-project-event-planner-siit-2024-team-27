package com.wde.eventplanner.models.reviews;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventReviewResponse {
    private UUID id;
    private int grade;
    private String comment;
    private UUID eventId;
    private UUID guestId;
    private String guestName;
    private String guestSurname;
}
