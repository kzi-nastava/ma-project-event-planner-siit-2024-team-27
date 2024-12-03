package com.wde.eventplanner.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Notification {
    private String id;
    private String title;
    private String message;
    private Date date;
    private boolean seen;
}
