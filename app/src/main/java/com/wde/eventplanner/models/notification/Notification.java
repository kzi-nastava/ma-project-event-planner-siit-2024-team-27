package com.wde.eventplanner.models.notification;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Notification {
    private UUID id;
    private String title;
    private String message;
    private LocalDateTime time;
    private boolean seen;
    private String type;
    private UUID entityId;
}
