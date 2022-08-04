package com.bzchao.chao.fangdao;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bzchao.webserver.client.WebServerRunner;
import com.bzchao.webserver.config.WebServerConfig;
import com.example.chao_common.utils.FileUtils;
import com.example.chao_device.DeviceActivity;
import com.example.chao_photo.PhotoActivity;
import com.example.httpserver.MyHttpServer2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tbruyelle.rxpermissions3.RxPermissions;


public class SettingActivity extends AppCompatActivity {
    public final static String TAG = "SettingActivity";

    private Button btnReg, btnHidden, btnShow, takePhotoPage, webServer, checkPermission;
    private SettingActivity context;
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rxPermissions = new RxPermissions(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        initBtn();
        initAllServer();
    }

    public void initBtn() {
        context = this;

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

        Button actionDevicePage = findViewById(R.id.actionDevicePage);
        actionDevicePage.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, DeviceActivity.class);
            startActivity(intent);
        });
        takePhotoPage = findViewById(R.id.takePhotoPage);
        takePhotoPage.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, PhotoActivity.class);
            startActivity(intent);
        });

        checkPermission = findViewById(R.id.checkPermission);
        checkPermission.setOnClickListener(v -> {
            rxPermissions
                    .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.WAKE_LOCK,
                            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                    .subscribe(permission -> {
                        Log.i(TAG, "Permission result " + permission + "," + permission.name);
                        if (permission.granted) {

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            Toast.makeText(SettingActivity.this,
                                    "Denied permission without ask never again",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            Toast.makeText(SettingActivity.this,
                                    "Permission denied, can't enable the " + permission.name,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            requestIgnoreBatteryOptimizations();
        });

        webServer = findViewById(R.id.webServer);
        webServer.setOnClickListener(v -> {
            try {
                MyHttpServer2 myHttpServer = new MyHttpServer2(context, 17000);
                myHttpServer.start(3000, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            new Thread(() -> {
                WebServerConfig.getInstance().setUPLOAD_DIR(FileUtils.getRootFile(context).getAbsolutePath());
                WebServerConfig.getInstance().setDISK_PATH(FileUtils.getRootFile(context).getAbsolutePath());
                WebServerConfig.getInstance().setWEB_DIR(FileUtils.getRootFile(context).getAbsolutePath() + "/00www");
                WebServerRunner.startServer();
            }).start();
            try {
                // 核心代码
                Uri uri = Uri.parse("http://127.0.0.1:" + WebServerConfig.getInstance().getPORT());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initAllServer() {
        try {
            MyHttpServer2 myHttpServer = new MyHttpServer2(context, 17000);
            myHttpServer.start(3000, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            WebServerConfig.getInstance().setUPLOAD_DIR(FileUtils.getRootFile(context).getAbsolutePath());
            WebServerConfig.getInstance().setDISK_PATH(FileUtils.getRootFile(context).getAbsolutePath());
            WebServerConfig.getInstance().setWEB_DIR(FileUtils.getRootFile(context).getAbsolutePath() + "/00www");
            WebServerRunner.startServer();
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.pessimism_settings:
                getAppDetailSettingIntent(context);
                return true;
            case R.id.notifi_settings:
                gotoNotificationAccessSetting(context);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void requestIgnoreBatteryOptimizations() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
