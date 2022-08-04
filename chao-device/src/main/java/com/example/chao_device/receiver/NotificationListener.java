package com.example.chao_device.receiver;

import android.content.Context;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;


public class NotificationListener extends NotificationListenerService {
    private static final String TAG = "NotificationListener";

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Log.e(TAG, "Notification removed");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //    Log.e(TAG, "Notification posted");
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate()");
        Context mContext = this.getApplicationContext();

        //  TODO
        //  new MyServiceManager(mContext).statService();
        super.onCreate();
    }
}