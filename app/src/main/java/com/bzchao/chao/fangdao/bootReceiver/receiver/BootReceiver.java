package com.bzchao.chao.fangdao.bootReceiver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bzchao.chao.fangdao.MyServiceManager;
import com.bzchao.chao.fangdao.Until.MyLog;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MyLog.e("BootReceiver", "onReceive() " + intent.getAction());
        new MyServiceManager(context).statService();
    }
}
