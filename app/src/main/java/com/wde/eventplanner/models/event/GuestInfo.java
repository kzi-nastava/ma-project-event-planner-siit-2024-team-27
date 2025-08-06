package com.wde.eventplanner.models.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GuestInfo {
    private String name;
    private String surname;
    private String email;
}
