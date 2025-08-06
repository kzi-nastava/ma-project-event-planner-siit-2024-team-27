package com.wde.eventplanner.models;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Page <T> {
    private ArrayList<T> content;
    private Integer totalPages;
}
