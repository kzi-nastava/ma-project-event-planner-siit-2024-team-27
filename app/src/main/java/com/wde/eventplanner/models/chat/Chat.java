package com.wde.eventplanner.models.chat;

import com.wde.eventplanner.models.listing.ListingType;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Chat {
    private UUID chatId;
    private String lastMessage;
    private String listingName;
    private UUID listingId;
    private String chatPartnerNameAndSurname;
    private UUID chatPartnerId;
    private LocalDateTime lastMessageDate;
    private ListingType listingType;
}
