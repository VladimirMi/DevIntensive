package com.softdesign.devintensive.data.tasks.events;

public class SavingUserDataEvent {
    public final boolean savingStatus;

    public SavingUserDataEvent(boolean savingStatus) {
        this.savingStatus = savingStatus;
    }
}
