package com.bzchao.chao.fangdao.photo;

import android.content.Context;
import android.content.Intent;

import com.bzchao.chao.fangdao.Until.MyLog;

public class MyPhotoManager {
    private static Context mContext;

    public MyPhotoManager(Context context) {
        this.mContext = context;
    }

    public void startService() {
        Intent intent = new Intent(mContext, new PhotoWindowService().getClass());
        mContext.getApplicationContext().startService(intent);
    }

    public void stopService() {
        Intent intent = new Intent(mContext, new PhotoWindowService().getClass());
        mContext.getApplicationContext().stopService(intent);
    }

    public void takePhoto() {
        if (PhotoWindowService.getInstance() != null) {
            PhotoWindowService.getInstance().cameraTakePhoto("chao");
        } else {
            MyLog.e("MyPhotoManager", "takePhoto() null");
            startService();
        }
    }
}