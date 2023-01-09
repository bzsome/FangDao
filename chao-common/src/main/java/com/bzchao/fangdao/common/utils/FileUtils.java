package com.bzchao.fangdao.common.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.util.Objects;

public class FileUtils {
    /**
     * 获取sd卡根目录
     */
    public static File getRootFile(Context context) {
        File file;
        if (Build.VERSION.SDK_INT >= 29) {
            // /storage/emulated/0/Android/data/com.example.demo/files
            File externalFileDir = context.getExternalFilesDir(null);
            do {
                externalFileDir = Objects.requireNonNull(externalFileDir).getParentFile();
            } while (Objects.requireNonNull(externalFileDir).getAbsolutePath().contains("/Android"));
            file = Objects.requireNonNull(externalFileDir);
        } else {
            file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile().toURI());
        }
        return file;
    }
}
