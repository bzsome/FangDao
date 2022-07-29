package com.bzchao.chao.fangdao;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bzchao.chao.fangdao.bootReceiver.device.MyDeviceManager;
import com.bzchao.chao.fangdao.photo.MyPhotoManager;
import com.bzchao.webserver.client.WebServerRunner;
import com.bzchao.webserver.config.WebServerConfig;
import com.example.chao_common.utils.FileUtils;
import com.example.httpserver.MyHttpServer2;
import com.example.httpserver.MyServer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class SettingActivity extends AppCompatActivity {
    public final static String TAG = "SettingActivity";

    private Button btnReg, btnHidden, btnShow, takePhoto, webServer;
    private SettingActivity context;
    private MyServer mMyServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        initBtn();

    }

    public void initBtn() {
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

        takePhoto = findViewById(R.id.takePhoto);
        takePhoto.setOnClickListener(v -> {
            new MyPhotoManager(context).takePhoto();
        });

        webServer = findViewById(R.id.webServer);
        webServer.setOnClickListener(v -> {
            try {
                mMyServer = new MyServer(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                MyHttpServer2 myHttpServer = new MyHttpServer2(context, 17000);
                myHttpServer.start(3000, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            new Thread(() -> {
                WebServerConfig.getInstance().setUPLOAD_DIR(FileUtils.getRootFile(context).getAbsolutePath());
                WebServerConfig.getInstance().setDISK_PATH(FileUtils.getRootFile(context).getAbsolutePath());
                WebServerConfig.getInstance().setWEB_DOC(FileUtils.getRootFile(context).getAbsolutePath() + "/00www");
                WebServerRunner.startServer();
            }).start();
            try {
                // 核心代码
                Uri uri = Uri.parse("http://127.0.0.1:" + WebServerConfig.getInstance().getPORT());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                // startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMyServer != null) {
            mMyServer.closeAllConnections();
            mMyServer = null;
            Log.e(TAG, "app pause, so web server close");
        }
    }

}
