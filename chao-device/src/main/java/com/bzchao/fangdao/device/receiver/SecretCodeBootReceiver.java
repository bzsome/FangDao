package com.bzchao.fangdao.device.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 监听暗码启动
 */
public class SecretCodeBootReceiver extends BroadcastReceiver {
    public static String SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE";
    private static final String TAG = "BootPhoneReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive()");
        if (intent.getAction() == null) {
            Log.e(TAG, "Action() null");
            return;
        }
        if (intent.getAction().equals(SECRET_CODE_ACTION)) {
            //TODO 监听
        }
    }
}
