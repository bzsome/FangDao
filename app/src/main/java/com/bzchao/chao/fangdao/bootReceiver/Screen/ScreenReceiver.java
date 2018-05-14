package com.bzchao.chao.fangdao.bootReceiver.Screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
            mStateListener.onScreenOn(context, intent);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            /**
             * 屏幕关闭
             */
            mStateListener.onScreenOff(context, intent);
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            /**
             * 屏幕解锁了且可以使用
             */
            mStateListener.onUserPresent(context, intent);
        }
    }

    public interface StateListener {
        /**
         * 此时屏幕已经点亮，但可能是在锁屏状态
         * 比如用户之前锁定了屏幕，按了电源键启动屏幕，则回调此方法
         */
        void onScreenOn(Context context, Intent intent);

        /**
         * 屏幕被锁定
         */
        void onScreenOff(Context context, Intent intent);

        /**
         * 屏幕解锁且可以正常使用
         */
        void onUserPresent(Context context, Intent intent);
    }
}
