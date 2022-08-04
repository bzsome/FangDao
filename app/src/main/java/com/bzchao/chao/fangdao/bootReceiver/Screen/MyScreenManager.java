package com.bzchao.chao.fangdao.bootReceiver.Screen;
/**
 * Created by Phil on 2017/7/18.
 */

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;

/**
 * 对于开发者来说，可能更多需要关注ScreenStateListener中的两个函数：
 * void onScreenOff(); 屏幕锁定
 * void onUserPresent(); 屏幕处于解锁状态且可以正常使用
 */
public class MyScreenManager {
    private static Context mContext;
    private static ScreenReceiver mScreenReceiver;

    public MyScreenManager(Context context) {
        mContext = context;
    }

    public void startReceiver() {
        Log.e("MyScreenManager", "startReceiver()");
        if (mScreenReceiver == null) {
            ScreenReceiver.StateListener mStateListener = getListener();
            mScreenReceiver = new ScreenReceiver(mStateListener);
            register();
        }
    }

    /**
     * 开始监听屏幕开/关状态
     */
    private void register() {
        //   Log.e("register()", "开始注册监听");
        if (mScreenReceiver != null) {
            /**
             * 注册屏幕设备开屏/锁屏的状态监听
             */
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            mContext.getApplicationContext().registerReceiver(mScreenReceiver, filter);
            //   initScreenState(); //可选
        }
    }

    private void initScreenState() {
        //初始状态设置
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        KeyguardManager mKeyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isKey = mKeyguardManager.inKeyguardRestrictedInputMode();
        if (pm.isScreenOn() && isKey) {//亮屏状态,且未解锁
            Log.e("MyScreenManager", "亮屏状态,且未解锁");
        }
    }

    /**
     * 注销屏幕设备开屏/锁屏的状态监听
     */
    public void unregister() {
        mContext.unregisterReceiver(mScreenReceiver);
        mScreenReceiver = null;
    }

    private ScreenReceiver.StateListener getListener() {
        return new ScreenReceiver.StateListener() {
            @Override
            public void onScreenOn(Context context, Intent intent) {
                Log.e("MyScreenManager", "屏幕亮起");
            }

            @Override
            public void onScreenOff(Context context, Intent intent) {
                Log.e("MyScreenManager", "屏幕关闭");
            }

            @Override
            public void onUserPresent(Context context, Intent intent) {
                Log.e("MyScreenManager", "屏幕解锁");
            }
        };
    }
}

