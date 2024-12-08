package com.wde.eventplanner.models.listing;

import androidx.annotation.NonNull;

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

