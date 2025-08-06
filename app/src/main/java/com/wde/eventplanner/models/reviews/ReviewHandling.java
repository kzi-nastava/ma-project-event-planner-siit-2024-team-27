package com.wde.eventplanner.models.reviews;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewHandling {
    private UUID id;
    private Boolean decision;
}
