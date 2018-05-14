package com.bzchao.chao.fangdao.bootReceiver.receiver;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.bzchao.chao.fangdao.MyServiceManager;
import com.bzchao.chao.fangdao.Until.MyLog;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {
    private static final String TAG = "NotificationListener";

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        MyLog.e(TAG, "Notification removed");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        MyLog.e(TAG, "Notification posted");
    }

    @Override
    public void onCreate() {
        MyLog.e(TAG, "onCreate()");
        Context mContext = this.getApplicationContext();
        new MyServiceManager(mContext).statService();
        super.onCreate();
    }
}