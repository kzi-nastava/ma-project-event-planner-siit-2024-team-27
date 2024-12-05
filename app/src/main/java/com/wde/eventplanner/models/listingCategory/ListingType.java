package com.wde.eventplanner.models.listingCategory;

import androidx.annotation.NonNull;

import lombok.ToString;

public enum ListingType {
    PRODUCT,
    SERVICE;

    @NonNull
    @Override
    public String toString() {
        switch (this.ordinal()) {
            case 0:
                return "Product";
            case 1:
                return "Service";
            default:
                return "";
        }
    }
}

