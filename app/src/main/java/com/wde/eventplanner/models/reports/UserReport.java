package com.wde.eventplanner.models.reports;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserReport {
    private UUID id;
    private String reason;
    private LocalDateTime reportDateTime;
    private LocalDateTime banStartDateTime;
    private PendingStatus status;
    private UUID userProfileToId;
    private UUID userProfileFromId;
}
