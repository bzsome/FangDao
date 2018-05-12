package com.bzchao.chao.fangdao.Screen;

import android.content.Context;
import android.content.Intent;

public interface StateListener {
    /**
     * 此时屏幕已经点亮，但可能是在锁屏状态
     * 比如用户之前锁定了屏幕，按了电源键启动屏幕，则回调此方法
     */
    void onScreenOn(Context context, Intent intent);

    /**
     * 屏幕被锁定
     */
    void onScreenOff(Context context, Intent intent);

    /**
     * 屏幕解锁且可以正常使用
     */
    void onUserPresent(Context context, Intent intent);
}

