package com.example.cloudpush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotfication extends FirebaseMessagingService {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
       String title = message.getNotification().getTitle();
       String body = message.getNotification().getBody();
       final String CHANNEL_ID = "HEADOFNOTIFCATION";
       NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                 "Heads Up Notification",
               NotificationManager.IMPORTANCE_HIGH
        );
       getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this,CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.googleg_standard_color_18)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1,notification.build());
       super.onMessageReceived(message);
    }
}
