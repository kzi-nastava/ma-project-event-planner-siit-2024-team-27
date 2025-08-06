package com.wde.eventplanner.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Comment {
    private String author;
    private String text;
}
