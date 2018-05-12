package com.bzchao.chao.fangdao.Screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bzchao.chao.fangdao.Until.MyLog;

/**
 * 设备屏幕状态广播接收者
 */
public class ScreenReceiver extends BroadcastReceiver {

    private static StateListener mStateListener;

    public ScreenReceiver(StateListener stateListener) {
        mStateListener = stateListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            /**
             * 屏幕亮
             */
           MyLog.e("ScreenReceiver", "屏幕亮起");
            mStateListener.onScreenOn(context, intent);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            /**
             * 屏幕关闭
             */
           MyLog.e("ScreenReceiver", "屏幕关闭");
            mStateListener.onScreenOff(context, intent);
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            /**
             * 屏幕解锁了且可以使用
             */
           MyLog.e("ScreenReceiver", "屏幕解锁");
            mStateListener.onUserPresent(context, intent);
        }
    }
}
