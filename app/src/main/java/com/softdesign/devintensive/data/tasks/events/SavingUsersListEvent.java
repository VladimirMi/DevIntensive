package com.softdesign.devintensive.data.tasks.events;

public class SavingUsersListEvent {
    public final String status;

    public SavingUsersListEvent(String status) {
        this.status = status;
    }
}
