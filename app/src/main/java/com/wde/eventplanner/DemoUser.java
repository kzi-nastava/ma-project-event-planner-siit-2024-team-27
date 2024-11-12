package com.wde.eventplanner;

public class DemoUser {
    private String email;
    private String password;

    public DemoUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
