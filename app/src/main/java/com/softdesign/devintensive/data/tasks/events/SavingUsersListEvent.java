package com.softdesign.devintensive.data.tasks.events;

public class SavingUsersListEvent {
    public final boolean savingStatus;

    public SavingUsersListEvent(boolean savingStatus) {
        this.savingStatus = savingStatus;
    }
}
