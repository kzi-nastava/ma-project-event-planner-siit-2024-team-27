package com.wde.eventplanner.models.user;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserBlock {
    private UUID fromId;
    private UUID toId;
}
