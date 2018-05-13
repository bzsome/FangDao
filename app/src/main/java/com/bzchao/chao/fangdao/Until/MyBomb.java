package com.bzchao.chao.fangdao.Until;

import android.content.Context;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

public class MyBomb {
    private static Context mContext;

    public MyBomb(Context context) {
        if (mContext == null) {
            Bmob.initialize(context, "664d253b58abf87ee1ac69cbef250e7e");
        }
        mContext = context;
    }

    public void upLoad(String filePath) {
        final BmobFile bmobFile = new BmobFile(new File(filePath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void onFinish() {
                MyLog.e("MyBomb", "onFinish 完成");
                super.onFinish();
            }

            @Override
            public void doneError(int code, String msg) {
                MyLog.e("MyBomb", "下载失败" + msg);
                super.doneError(code, msg);
            }

            @Override
            public void done(BmobException e) {
                MyLog.e("MyBomb", "上传完成！");
                MyLog.e("上传链接", bmobFile.getFileUrl());
            }
        });

    }
}
