package com.bzchao.fangdao.camera.photo;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

import com.bzchao.fangdao.camera.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class PhotoWindowManager {
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * 屏幕高度
     */
    private int screenHeight;
    /**
     * 小悬浮窗View的布局
     */
    private FrameLayout frameLayout;
    private WindowManager mWindowManager;
    private Context mContext;
    /**
     * 包裹SurfaceView的容器
     */
    private FrameLayout mSurfaceContainer;

    public PhotoWindowManager(Context context) {
        mContext = context;
        Intent intent = new Intent(context, new PhotoWindowService().getClass());
        mContext.startService(intent);
    }

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public void createSmallWindow(Context context) {
        mContext = context;
        WindowManager windowManager = getWindowManager(context);
        // 这里多加了一层布局,可以从布局里面获取宽高不需要写固定的
        frameLayout = new FrameLayout(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, frameLayout);
        View containerView = frameLayout.findViewById(R.id.small_window_layout);
        int viewWidth = containerView.getLayoutParams().width;
        int viewHeight = containerView.getLayoutParams().height;
        LayoutParams layoutParams = new LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = LayoutParams.TYPE_PHONE;// 系统提示window
        }

        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;

        layoutParams.width = viewWidth;
        layoutParams.height = viewHeight;
        layoutParams.x = screenWidth / 2;
        layoutParams.y = screenHeight;
        windowManager.addView(frameLayout, layoutParams);
        mSurfaceContainer = frameLayout.findViewById(R.id.percent);
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public void removeSmallWindow(Context context) {
        if (frameLayout != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(frameLayout);
            frameLayout = null;
        }
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = mWindowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            screenWidth = metrics.widthPixels;
            screenHeight = metrics.heightPixels;
        }
        return mWindowManager;
    }

    public FrameLayout getSurfaceContainer() {
        return mSurfaceContainer;
    }

    /***
     * 检查悬浮窗开启权限
     * @param context
     * @return
     */
    public boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                return result == declaredField2.getInt(cls);
            } catch (Exception e) {
                return false;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName());
                return Settings.canDrawOverlays(context) || mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
            } else {
                return Settings.canDrawOverlays(context);
            }
        }
    }

    /**
     * 悬浮窗开启权限
     * @param context
     * @param requestCode
     */
    public void requestFloatPermission(Activity context, int requestCode){
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivityForResult(intent, requestCode);
    }
}
