package com.wde.eventplanner.models.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationRequest {
    private String email;
    private String password;
    private boolean isActive;
    private boolean areNotificationsMuted;
    private String userType;
    private String name;
    private String surname;
    private String city;
    private String address;
    private String telephoneNumber;
}
