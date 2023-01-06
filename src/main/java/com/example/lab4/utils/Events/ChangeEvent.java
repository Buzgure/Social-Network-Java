package com.example.lab4.utils.Events;

import com.example.lab4.Domain.Entity;
import com.example.lab4.Domain.FriendshipForDB;

public class ChangeEvent implements Event{

    private ChangeEventType type;

    private FriendshipForDB data, oldData;

    public ChangeEvent(ChangeEventType type, FriendshipForDB data, FriendshipForDB oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEvent(ChangeEventType type, FriendshipForDB data) {
        this.type = type;
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public void setType(ChangeEventType type) {
        this.type = type;
    }

    public FriendshipForDB getData() {
        return data;
    }

    public void setData(FriendshipForDB data) {
        this.data = data;
    }

    public FriendshipForDB getOldData() {
        return oldData;
    }

    public void setOldData(FriendshipForDB oldData) {
        this.oldData = oldData;
    }
}
