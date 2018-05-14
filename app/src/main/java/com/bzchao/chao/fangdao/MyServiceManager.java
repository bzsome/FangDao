package com.bzchao.chao.fangdao;

import android.content.Context;
import android.content.Intent;

import com.bzchao.chao.fangdao.Until.MyLog;
import com.bzchao.chao.fangdao.bootReceiver.Screen.MyScreenManager;
import com.bzchao.chao.fangdao.keepalive.DaemonService;
import com.bzchao.chao.fangdao.keepalive.PlayerMusicService;

public class MyServiceManager {
    private static Context mContext;
    private final static String TAG = "MyServiceManager";

    public MyServiceManager(Context context) {
        mContext = context;
    }

    public void statService() {
        MyLog.e(TAG, "statService()");
        startDaemonService();
        startPlayMusicService();
        startScreenService();
    }

    public void startPlayMusicService() {
        if (!PlayerMusicService.getIsLive()) {
            Intent intent = new Intent(mContext, PlayerMusicService.class);
            mContext.getApplicationContext().startService(intent);
        }
    }

    public void startDaemonService() {
        if (!DaemonService.getIsLive()) {
            Intent intent = new Intent(mContext, DaemonService.class);
            mContext.getApplicationContext().startService(intent);
        }
    }

    public void startScreenService() {
        new MyScreenManager(mContext).startReceiver();
    }
}