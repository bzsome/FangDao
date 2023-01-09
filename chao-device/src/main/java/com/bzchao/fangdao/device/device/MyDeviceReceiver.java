package com.bzchao.fangdao.device.device;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class MyDeviceReceiver extends DeviceAdminReceiver {
    @Override
    public DevicePolicyManager getManager(Context context) {
        Log.e("AdminReceiver", "------" + "getManager" + "------");
        return super.getManager(context);
    }

    @Override
    public ComponentName getWho(Context context) {
        Log.e("AdminReceiver", "------" + "getWho" + "------");
        return super.getWho(context);
    }

    /**
     * 禁用
     */
    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.e("AdminReceiver", "------" + "onDisabled" + "------");
        super.onDisabled(context, intent);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        Log.e("AdminReceiver", "------" + "onDisableRequested" + "------");
        new MyDeviceManager(context).lockDevice();
        return "";
    }

    /**
     * 激活
     */
    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.e("AdminReceiver", "------" + "onEnabled" + "------");
        Toast.makeText(context, "启动设备管理", Toast.LENGTH_SHORT).show();
        super.onEnabled(context, intent);
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        Log.e("AdminReceiver", "------" + "onPasswordChanged" + "------");
        super.onPasswordChanged(context, intent);
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        Log.e("AdminReceiver", "------" + "onPasswordFailed" + "------");
        super.onPasswordFailed(context, intent);
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        Log.e("AdminReceiver", "------" + "onPasswordSucceeded" + "------");
        super.onPasswordSucceeded(context, intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("AdminReceiver", "------" + "onReceive" + "------");
        //TODO 处理事件
        super.onReceive(context, intent);
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        Log.e("AdminReceiver", "------" + "peekService" + "------");
        return super.peekService(myContext, service);
    }
}