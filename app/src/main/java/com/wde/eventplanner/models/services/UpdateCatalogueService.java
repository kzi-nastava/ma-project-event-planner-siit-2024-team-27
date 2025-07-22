package com.wde.eventplanner.models.services;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCatalogueService {
    private List<CatalogueService> toBeUpdated;
}
