package com.bzchao.fangdao.keepalive.job;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.bzchao.fangdao.keepalive.daemonservice.DaemonServiceManager;
import com.bzchao.fangdao.keepalive.utils.Constants;
import com.bzchao.fangdao.keepalive.utils.SystemUtils;

/**
 * JobService，支持5.0以上forcestop依然有效
 * 通过定时任务不断重启app
 * <p>
 * Created by jianddongguo on 2017/7/10.
 */
@TargetApi(21)
public class AliveJobService extends JobService {
    private final static String TAG = "KeepAliveService";
    // 告知编译器，这个变量不能被优化
    private volatile static Service mKeepAliveService = null;

    public static boolean isJobServiceAlive() {
        return mKeepAliveService != null;
    }

    private static final int MESSAGE_ID_TASK = 0x01;

    private Handler mHandler = new Handler(msg -> {
        // 具体任务逻辑
        if (SystemUtils.isAppAlive(getApplicationContext(), Constants.PACKAGE_NAME)) {
            Toast.makeText(getApplicationContext(), "APP活着的", Toast.LENGTH_SHORT).show();
        } else {
            new DaemonServiceManager(getApplicationContext()).statService();
            Toast.makeText(getApplicationContext(), "APP被杀死，重启服务...", Toast.LENGTH_SHORT).show();
        }
        // 通知系统任务执行结束
        jobFinished((JobParameters) msg.obj, false);
        return true;
    });

    @Override
    public boolean onStartJob(JobParameters params) {
        if (Constants.DEBUG) Log.d(TAG, "KeepAliveService----->JobService服务被启动...");
        mKeepAliveService = this;
        // 返回false，系统假设这个方法返回时任务已经执行完毕；
        // 返回true，系统假定这个任务正要被执行
        Message msg = Message.obtain(mHandler, MESSAGE_ID_TASK, params);
        mHandler.sendMessage(msg);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mHandler.removeMessages(MESSAGE_ID_TASK);
        if (Constants.DEBUG) Log.d(TAG, "KeepAliveService----->JobService服务被关闭");
        return false;
    }
}
