package com.example.mobile_shopping;

import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("New Friend Request!")
                .setContentText("You have received a new friend request.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMng.notify(mNotificationId, builder.build());

    }
}
