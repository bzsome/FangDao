package com.bzchao.fangdao.camera.photo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyPhotoManager {
    private static Context mContext;

    public MyPhotoManager(Context context) {
        this.mContext = context;
    }

    public void startService() {
        Intent intent = new Intent(mContext, PhotoWindowService.class);
        mContext.getApplicationContext().startService(intent);
    }

    public void stopService() {
        Intent intent = new Intent(mContext, PhotoWindowService.class);
        mContext.getApplicationContext().stopService(intent);
    }

    public void takePhoto() {
        try {
            if (PhotoWindowService.getInstance() == null) {
                startService();
            }
            if (PhotoWindowService.getInstance() != null) {
                PhotoWindowService.getInstance().cameraTakePhoto("chao");
            }
        } catch (Exception e) {
            Log.e("MyPhotoManager", e.getMessage(), e);
        }
    }
}