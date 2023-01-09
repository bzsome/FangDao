package com.bzchao.fangdao.keepalive.singepixel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.bzchao.fangdao.keepalive.utils.Constants;
import com.bzchao.fangdao.keepalive.utils.SystemUtils;

/**
 * 1像素Activity
 * <p>
 * Created by jianddongguo on 2017/7/8.
 */

public class SinglePixelActivity extends AppCompatActivity {
    private static final String TAG = "SinglePixelActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constants.DEBUG)
            Log.d(TAG, "onCreate--->启动1像素保活");
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 300;
        attrParams.width = 300;
        mWindow.setAttributes(attrParams);
        // 绑定SinglePixelActivity到ScreenManager
        SingePixelManager.getScreenManagerInstance(this).setSingleActivity(this);
    }

    @Override
    protected void onDestroy() {
        if (Constants.DEBUG)
            Log.d(TAG, "onDestroy--->1像素保活被终止");
        if (!SystemUtils.isAppAlive(this, Constants.PACKAGE_NAME)) {
            Intent intentAlive = new Intent(this, SinglePixelActivity.class);
            intentAlive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentAlive);
            Log.i(TAG, "SinglePixelActivity---->APP被干掉了，我要重启它");
        }
        super.onDestroy();
    }
}
