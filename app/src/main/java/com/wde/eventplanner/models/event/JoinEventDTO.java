package com.wde.eventplanner.models.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JoinEventDTO {
    private String guestId;
    private String eventId;
}
