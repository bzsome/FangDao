package com.example.chao_device;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chao_device.device.MyDeviceManager;

public class DeviceActivity extends AppCompatActivity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        activity = this;

        Button btnReg = findViewById(R.id.actionDevice);
        btnReg.setOnClickListener(v -> new MyDeviceManager(activity).registerDevicePolicyManager());

        Button disableDevice = findViewById(R.id.disableDevice);
        disableDevice.setOnClickListener(v -> showRemoveDevice());

    }

    public void showRemoveDevice() {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("提示")//设置对话框的标题
                .setMessage("是否禁用设备管理器？")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog1, which) -> {
                    new MyDeviceManager(activity).onRemoveActivate();
                    Toast.makeText(activity, "已禁用设备管理器", Toast.LENGTH_SHORT).show();
                    dialog1.dismiss();
                }).create();
        dialog.show();
    }

}