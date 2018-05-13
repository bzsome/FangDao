package com.bzchao.chao.fangdao.device;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.bzchao.chao.fangdao.Until.MyLog;

public class MyDeviceManager {
    private Context context;
    private ComponentName componentName;//权限监听器
    private DevicePolicyManager devicePolicyManager;
    private static boolean isLock = true;

    public MyDeviceManager(Context context) {
        this.context = context;
        componentName = new ComponentName(context, MyDeviceReceiver.class);
        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
    }


    /**
     * 注册成为"设备管理器"
     */
    public void registerDevicePolicyManager() {
        try {
            //检测是否已经是设备管理器
            if (!isAdminActive()) {
                activeAdmin();
                MyLog.e("registerDeviceManager", "打开注册页面");
            } else {
                // 已经是设备管理器了，就可以操作一些特殊的安全权限了
                Toast.makeText(context, "设备已经激活,请勿重复激活", Toast.LENGTH_SHORT).show();
                MyLog.e("registerDeviceManager", "已注册设备管理器");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAdminActive() { // 实例化系统的设备管理器
        return devicePolicyManager.isAdminActive(componentName);
    }


    /**
     * 激活设备管理器
     */
    private void activeAdmin() {
        //指定广播接收器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "hello~~,设备管理器");
        context.startActivity(intent);
    }

    /**
     * 移除程序 如果不移除程序 APP无法被卸载
     */
    public void onRemoveActivate() {
        isLock = false;
        devicePolicyManager.removeActiveAdmin(componentName);
    }

    public void lockDevice() {
        if (!isLock) {//不锁定
            return;
        }
        //跳离当前询问是否取消激活的 dialog
        Intent outOfDialog = context.getPackageManager().getLaunchIntentForPackage("com.android.settings");
        outOfDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(outOfDialog);

        //调用设备管理器本身的功能，每 100ms 锁屏一次，用户即便解锁也会立即被锁，直至 7s 后
        final DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        dpm.lockNow();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i < 70) {
                    dpm.lockNow();
                    try {
                        Thread.sleep(100);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        try {
            int i = 0;
            while (i < 70) {
                dpm.lockNow();
                try {
                    Thread.sleep(100);
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
