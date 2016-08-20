package com.onefengma.taobuxiu.manager.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.R;

/**
 * Created by chufengma on 16/8/20.
 */
public class NotificationHelper {

    public static void showNotification(String title, String text, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) MainApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(MainApplication.getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(MainApplication.getContext())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText(text).build();

        notificationManager.notify(0, notification);
    }

}
