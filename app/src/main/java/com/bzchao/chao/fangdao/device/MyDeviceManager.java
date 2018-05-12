package com.bzchao.chao.fangdao.device;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.bzchao.chao.fangdao.Until.MyLog;

public class MyDeviceManager {
    private Context context;
    private ComponentName componentName;//权限监听器
    private DevicePolicyManager devicePolicyManager;

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
        devicePolicyManager.removeActiveAdmin(componentName);
    }
}
