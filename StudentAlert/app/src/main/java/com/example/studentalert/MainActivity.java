package com.example.studentalert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;
    private List<Alarm> alarmList = new ArrayList<>();
    private int alarmId = 0;
    private static final int REQUEST_CODE_EXACT_ALARM_PERMISSION = 1001;
    private Calendar selectedTime = Calendar.getInstance();
    private EditText eventInput;
    private Button timeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        alarmAdapter = new AlarmAdapter(alarmList);
        recyclerView.setAdapter(alarmAdapter);

        timeButton = findViewById(R.id.timeButton);
        eventInput = findViewById(R.id.eventInput);
        Button addButton = findViewById(R.id.addButton);

        timeButton.setOnClickListener(v -> showTimePickerDialog());

        addButton.setOnClickListener(v -> {
            String eventName = eventInput.getText().toString();
            if (eventName.isEmpty()) {
                eventName = "Alarm triggered!";
            }

            Alarm alarm = new Alarm(alarmId++, selectedTime.getTimeInMillis(), eventName);
            AlarmRepository.addAlarm(alarm); // Add to repository
            alarmList.add(alarm);

            // Sort alarms by time
            Collections.sort(alarmList, (a1, a2) -> Long.compare(a1.getTimeInMillis(), a2.getTimeInMillis()));

            alarmAdapter.notifyDataSetChanged();
            setAlarm(alarm);
        });
    }

    private void showTimePickerDialog() {
        int hour = selectedTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minuteOfHour) -> {
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedTime.set(Calendar.MINUTE, minuteOfHour);
                    selectedTime.set(Calendar.SECOND, 0);

                    // Update the button text to show selected time
                    String amPm = hourOfDay >= 12 ? "PM" : "AM";
                    int hour12 = hourOfDay % 12;
                    hour12 = (hour12 == 0) ? 12 : hour12; // Handle midnight and noon
                    timeButton.setText(String.format("%02d:%02d %s", hour12, minuteOfHour, amPm));
                },
                hour,
                minute,
                false // Use 24-hour format (set to true for 24-hour format)
        );

        timePickerDialog.show();
    }

    private void setAlarm(Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("EVENT_NAME", alarm.getEventName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);
            } else {
                // Request permission to schedule exact alarms
                Intent permissionIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivityForResult(permissionIntent, REQUEST_CODE_EXACT_ALARM_PERMISSION);
            }
        } else {
            // For devices below API level 31, use setExact
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);
        }
    }
}
