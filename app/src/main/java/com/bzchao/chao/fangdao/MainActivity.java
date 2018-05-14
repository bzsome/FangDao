package com.bzchao.chao.fangdao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 给bnt1添加点击响应事件
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        //启动
        startActivity(intent);
    }
}
