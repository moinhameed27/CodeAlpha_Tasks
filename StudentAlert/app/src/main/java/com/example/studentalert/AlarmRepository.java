package com.example.studentalert;

import java.util.ArrayList;
import java.util.List;

public class AlarmRepository {

    private static List<Alarm> alarms = new ArrayList<>();

    // Add an alarm to the repository
    public static void addAlarm(Alarm alarm) {
        alarms.add(alarm);
    }

    // Get all alarms from the repository
    public static List<Alarm> getAlarms() {
        return alarms;
    }

    // Remove an alarm from the repository by ID
    public static void removeAlarm(int id) {
        for (Alarm alarm : alarms) {
            if (alarm.getId() == id) {
                alarms.remove(alarm);
                break;
            }
        }
    }
}
