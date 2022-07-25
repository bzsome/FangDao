package com.bzchao.chao.fangdao;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bzchao.chao.fangdao.bootReceiver.device.MyDeviceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class SettingActivity extends AppCompatActivity {
    Button btnReg, btnHidden, btnShow;
    SettingActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        init();
    }

    public void init() {
        context = this;
        btnReg = findViewById(R.id.actionDevice);
        btnReg.setOnClickListener(v -> new MyDeviceManager(context).registerDevicePolicyManager());

        btnHidden = findViewById(R.id.hiddenDevice);
        btnHidden.setOnClickListener(v -> {
            PackageManager p = context.getPackageManager();
            p.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, PackageManager.DONT_KILL_APP);
        });
        btnShow = findViewById(R.id.showDevice);
        btnShow.setOnClickListener(v -> {
            PackageManager p = context.getPackageManager();
            p.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.pessimism_settings:
                getAppDetailSettingIntent(context);
                return true;
            case R.id.notifi_settings:
                gotoNotificationAccessSetting(context);
                return true;
            case R.id.remove_device:
                showRemoveDevice();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    /**
     * 跳转到权限设置界面
     */
    private void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }

    private boolean gotoNotificationAccessSetting(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                context.startActivity(intent);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    public void showRemoveDevice() {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setIcon(R.mipmap.ic_launcher)//设置标题的图片
                .setTitle("提示")//设置对话框的标题
                .setMessage("是否禁用设备管理器？")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog1, which) -> {
                    new MyDeviceManager(context).onRemoveActivate();
                    Toast.makeText(context, "已禁用设备管理器", Toast.LENGTH_SHORT).show();
                    dialog1.dismiss();
                }).create();
        dialog.show();
    }
}
