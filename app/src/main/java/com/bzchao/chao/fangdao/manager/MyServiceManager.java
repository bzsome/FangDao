package com.bzchao.chao.fangdao.manager;

import android.content.Context;

import com.bzchao.chao.fangdao.Screen.ScreenManager;

public class MyServiceManager {
    private final Context mContext;

    MyServiceManager(Context context) {
        this.mContext = context;
    }

    public void startService() {
        new ScreenManager(mContext).startReceiver();
    }

    public void stopService() {
        new MyPhotoManager(mContext).stopService();
    }
}
