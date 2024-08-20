package com.example.studentalert;

public class Alarm {
    private int id;
    private long timeInMillis;
    private String eventName;

    public Alarm(int id, long timeInMillis, String eventName) {
        this.id = id;
        this.timeInMillis = timeInMillis;
        this.eventName = eventName;
    }

    public int getId() {
        return id;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public String getEventName() {
        return eventName;
    }
}
