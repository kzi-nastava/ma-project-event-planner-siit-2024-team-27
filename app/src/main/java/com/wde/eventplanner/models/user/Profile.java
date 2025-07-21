package com.wde.eventplanner.models.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    private String email;
    private String password;
    private Boolean isActive;
    private Boolean areNotificationsMuted;
    private String userType;
    private String name;
    private String surname;
    private String city;
    private String address;
    private String telephoneNumber;
    private String description;
}
