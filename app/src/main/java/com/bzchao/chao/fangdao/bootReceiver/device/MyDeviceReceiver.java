package com.bzchao.chao.fangdao.bootReceiver.device;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.bzchao.chao.fangdao.MyServiceManager;
import com.bzchao.chao.fangdao.Until.MyLog;

public class MyDeviceReceiver extends DeviceAdminReceiver {
    @Override
    public DevicePolicyManager getManager(Context context) {
        MyLog.e("AdminReceiever", "------" + "getManager" + "------");
        return super.getManager(context);
    }

    @Override
    public ComponentName getWho(Context context) {
        MyLog.e("AdminReceiever", "------" + "getWho" + "------");
        return super.getWho(context);
    }

    /**
     * 禁用
     */
    @Override
    public void onDisabled(Context context, Intent intent) {
        MyLog.e("AdminReceiever", "------" + "onDisabled" + "------");
        super.onDisabled(context, intent);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        MyLog.e("AdminReceiever", "------" + "onDisableRequested" + "------");
        new MyDeviceManager(context).lockDevice();
        return "";
    }

    /**
     * 激活
     */
    @Override
    public void onEnabled(Context context, Intent intent) {
        MyLog.e("AdminReceiever", "------" + "onEnabled" + "------");
        Toast.makeText(context, "启动设备管理", Toast.LENGTH_SHORT).show();
        super.onEnabled(context, intent);
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        MyLog.e("AdminReceiever", "------" + "onPasswordChanged" + "------");
        super.onPasswordChanged(context, intent);
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        MyLog.e("AdminReceiever", "------" + "onPasswordFailed" + "------");
        super.onPasswordFailed(context, intent);
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        MyLog.e("AdminReceiever", "------" + "onPasswordSucceeded" + "------");
        super.onPasswordSucceeded(context, intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MyLog.e("AdminReceiever", "------" + "onReceive" + "------");
        new MyServiceManager(context).statService();
        super.onReceive(context, intent);
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        MyLog.e("AdminReceiever", "------" + "peekService" + "------");
        return super.peekService(myContext, service);
    }
}