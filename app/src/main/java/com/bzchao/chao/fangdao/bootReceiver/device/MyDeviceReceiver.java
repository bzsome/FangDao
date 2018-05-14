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
import com.bzchao.chao.fangdao.photo.MyPhotoManager;

public class MyDeviceReceiver extends DeviceAdminReceiver {
    @Override
    public DevicePolicyManager getManager(Context context) {
        MyLog.e("AdminReciever", "------" + "getManager" + "------");
        return super.getManager(context);
    }

    @Override
    public ComponentName getWho(Context context) {
        MyLog.e("AdminReciever", "------" + "getWho" + "------");
        return super.getWho(context);
    }

    /**
     * 禁用
     */
    @Override
    public void onDisabled(Context context, Intent intent) {
        MyLog.e("AdminReciever", "------" + "onDisabled" + "------");
        new MyDeviceManager(context).lockDevice();
        super.onDisabled(context, intent);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        MyLog.e("AdminReciever", "------" + "onDisableRequested" + "------");
        return super.onDisableRequested(context, intent);
    }

    /**
     * 激活
     */
    @Override
    public void onEnabled(Context context, Intent intent) {
        MyLog.e("AdminReciever", "------" + "onEnabled" + "------");
        Toast.makeText(context, "启动设备管理", Toast.LENGTH_SHORT).show();
        super.onEnabled(context, intent);
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        MyLog.e("AdminReciever", "------" + "onPasswordChanged" + "------");
        super.onPasswordChanged(context, intent);
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        MyLog.e("AdminReciever", "------" + "onPasswordFailed" + "------");
        new MyPhotoManager(context).takePhoto();
        super.onPasswordFailed(context, intent);
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        MyLog.e("AdminReciever", "------" + "onPasswordSucceeded" + "------");
        super.onPasswordSucceeded(context, intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MyLog.e("AdminReciever", "------" + "onReceive" + "------");
        new MyServiceManager(context).statService();
        super.onReceive(context, intent);
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        MyLog.e("AdminReciever", "------" + "peekService" + "------");
        return super.peekService(myContext, service);
    }
}