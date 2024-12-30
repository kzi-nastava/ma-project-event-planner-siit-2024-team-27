package com.wde.eventplanner.models.event;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendaItem {
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String location;

    public boolean isFilled() {
        return name != null && !name.isBlank() &&
                description != null && !description.isBlank() &&
                startTime != null && !startTime.isBlank() &&
                endTime != null && !endTime.isBlank() &&
                location != null && !location.isBlank();
    }
}
