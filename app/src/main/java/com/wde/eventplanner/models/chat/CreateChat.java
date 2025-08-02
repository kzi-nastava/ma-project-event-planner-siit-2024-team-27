package com.wde.eventplanner.models.chat;

import com.wde.eventplanner.models.listing.ListingType;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateChat {
    private ListingType listingType;
    private UUID listingId;
    private Integer listingVersion;
    private UUID chatter1Id;
    private UUID chatter2Id;
}
