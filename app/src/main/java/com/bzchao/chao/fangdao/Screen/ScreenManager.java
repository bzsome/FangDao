package com.bzchao.chao.fangdao.Screen;
/**
 * Created by Phil on 2017/7/18.
 */

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import com.bzchao.chao.fangdao.Until.MyLog;
import com.bzchao.chao.fangdao.manager.MyPhotoManager;

/**
 * 对于开发者来说，可能更多需要关注ScreenStateListener中的两个函数：
 * void onScreenOff(); 屏幕锁定
 * void onUserPresent(); 屏幕处于解锁状态且可以正常使用
 */
public class ScreenManager {
    private static Context mContext;
    private static ScreenReceiver mScreenReceiver;

    public ScreenManager(Context context) {
        mContext = context;
    }

    public void startReceiver() {
        MyLog.e("ScreenManager", "startReceiver()");
        mScreenReceiver = new ScreenReceiver(new StateListener() {
            @Override
            public void onScreenOn(Context context, Intent intent) {
                MyLog.e("ScreenManager", "屏幕亮起");
                new MyPhotoManager(context).startService();
            }

            @Override
            public void onScreenOff(Context context, Intent intent) {
                MyLog.e("ScreenManager", "屏幕关闭");
                new MyPhotoManager(context).stopService();
            }

            @Override
            public void onUserPresent(Context context, Intent intent) {
                MyLog.e("ScreenManager", "屏幕解锁");
                new MyPhotoManager(context).stopService();
            }
        });
        register();
    }

    /**
     * 开始监听屏幕开/关状态
     */
    private void register() {
        if (mScreenReceiver != null) {
            /**
             * 注册屏幕设备开屏/锁屏的状态监听
             */
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            mContext.getApplicationContext().registerReceiver(mScreenReceiver, filter);
            initScreenState(); //可选
        }
    }

    private void initScreenState() {
        //初始状态设置
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        KeyguardManager mKeyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isKey = mKeyguardManager.inKeyguardRestrictedInputMode();
        if (pm.isScreenOn() && isKey) {//亮屏状态,且未解锁
            MyLog.e("ScreenManager", "亮屏状态,且未解锁");
            new MyPhotoManager(mContext).startService();
        }
    }

    /**
     * 注销屏幕设备开屏/锁屏的状态监听
     */
    public void unregister() {
        mContext.unregisterReceiver(mScreenReceiver);
        mScreenReceiver = null;
    }
}

