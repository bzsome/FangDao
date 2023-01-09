package com.bzchao.fangdao.keepalive.daemonservice;

import android.content.Context;
import android.content.Intent;

import com.bzchao.fangdao.common.utils.MyLog;
import com.bzchao.fangdao.keepalive.music.PlayerMusicService;

public class DaemonServiceManager {
    private static Context mContext;
    private final static String TAG = "MyServiceManager";

    public DaemonServiceManager(Context context) {
        mContext = context;
    }

    public void statService() {
        MyLog.e(TAG, "statService()");
        startDaemonService();
        startPlayMusicService();
        startScreenService();
    }

    public void stopService() {
        MyLog.e(TAG, "stopService()");
        stopDaemonService();
        stopPlayMusicService();
        stopScreenService();
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
        //        new MyScreenManager(mContext).startReceiver();
    }

    public void stopPlayMusicService() {
        if (PlayerMusicService.getIsLive()) {
            Intent intent = new Intent(mContext, PlayerMusicService.class);
            mContext.getApplicationContext().stopService(intent);
        }
    }

    public void stopDaemonService() {
        if (DaemonService.getIsLive()) {
            Intent intent = new Intent(mContext, DaemonService.class);
            mContext.getApplicationContext().stopService(intent);
        }
    }

    public void stopScreenService() {
        //    new MyScreenManager(mContext).unregister();
    }
}