package com.example.mobile_shopping;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String _mNotificationTitle = remoteMessage.getNotification().getTitle();
        String _mNofiticationMessage = remoteMessage.getNotification().getBody();

        String _fromUserId = remoteMessage.getData().get("from_user_id");
        String click_act = remoteMessage.getNotification().getClickAction();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(_mNotificationTitle)
                .setContentText(_mNofiticationMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent resultIntent = new Intent(click_act);
        resultIntent.putExtra("user_id", _fromUserId);

        System.out.println("user_id : " + _fromUserId);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMng.notify(mNotificationId, builder.build());

        builder.setContentIntent(resultPendingIntent);

    }
}
