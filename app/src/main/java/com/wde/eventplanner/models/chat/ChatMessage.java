package com.wde.eventplanner.models.chat;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessage {
    private UUID chatId;
    private String message;
    private UUID toProfileId;
}
