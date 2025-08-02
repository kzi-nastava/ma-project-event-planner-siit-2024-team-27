package com.wde.eventplanner.models.reports;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReportResponse {
    private UUID id;
    private String reason;
    private LocalDateTime reportDateTime;
    private String profileToCredentials;
    private String profileFromCredentials;
}
