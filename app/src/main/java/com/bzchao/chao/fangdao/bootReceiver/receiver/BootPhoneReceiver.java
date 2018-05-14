package com.bzchao.chao.fangdao.bootReceiver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bzchao.chao.fangdao.MainActivity;
import com.bzchao.chao.fangdao.MyServiceManager;
import com.bzchao.chao.fangdao.Until.MyLog;

public class BootPhoneReceiver extends BroadcastReceiver {
    public static String SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE";
    private static final String TAG = "BootPhoneReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        MyLog.e(TAG, "onReceive()");
        if (intent.getAction() == null) {
            MyLog.e(TAG, "Action() null");
            return;
        }
        if (intent.getAction().equals(SECRET_CODE_ACTION)) {
            new MyServiceManager(context).statService();
        }
    }
}
