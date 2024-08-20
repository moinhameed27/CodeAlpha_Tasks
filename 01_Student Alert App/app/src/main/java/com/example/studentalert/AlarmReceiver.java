package com.example.studentalert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "ALARM_CHANNEL_ID";
    private static final String CHANNEL_NAME = "Alarm Channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String eventName = intent.getStringExtra("EVENT_NAME");
        if (eventName == null || eventName.isEmpty()) {
            eventName = "Alarm triggered!";
        }

        // Create a notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Student Alert")
                .setContentText(eventName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Show the notification
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());

        // Show toast as a fallback
        Toast.makeText(context, eventName, Toast.LENGTH_SHORT).show();
    }
}
