package com.wde.eventplanner.models.serviceBudgetItem;

import com.wde.eventplanner.models.event.Event;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookingSlots {
    private Event event;
    private HashMap<String, ArrayList<String>> timeTable;
}
