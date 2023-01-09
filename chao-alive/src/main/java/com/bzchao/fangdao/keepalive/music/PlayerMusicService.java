package com.bzchao.fangdao.keepalive.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.bzchao.fangdao.common.utils.MyLog;
import com.bzchao.fangdao.keepalive.R;
import com.bzchao.fangdao.keepalive.utils.Constants;


/**
 * 循环播放一段无声音频，以提升进程优先级
 * <p>
 * Created by jianddongguo on 2017/7/11.
 * http://blog.csdn.net/andrexpert
 */

public class PlayerMusicService extends Service {
    private final static String TAG = "PlayerMusicService";
    private MediaPlayer mMediaPlayer;
    //储存服务状态
    private static boolean isAlive = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        isAlive = true;
        super.onCreate();
        if (Constants.DEBUG) MyLog.e(TAG, "---->onCreate,启动服务");
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent);
        mMediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> startPlayMusic()).start();
        return START_STICKY;
    }

    private void startPlayMusic() {
        if (mMediaPlayer != null) {
            if (Constants.DEBUG) Log.d(TAG, "启动后台播放音乐");
            mMediaPlayer.start();
        }
    }

    private void stopPlayMusic() {
        if (mMediaPlayer != null) {
            if (Constants.DEBUG) Log.d(TAG, "关闭后台播放音乐");
            mMediaPlayer.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isAlive = false;
        stopPlayMusic();
        if (Constants.DEBUG) MyLog.e(TAG, "---->onCreate,停止服务");
        // 重启自己
        Intent intent = new Intent(getApplicationContext(), PlayerMusicService.class);
        startService(intent);
    }

    public static boolean getIsLive() {
        return isAlive;
    }
}
