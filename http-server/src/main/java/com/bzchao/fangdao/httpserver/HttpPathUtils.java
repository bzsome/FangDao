package com.bzchao.fangdao.httpserver;

import android.content.Context;

import com.bzchao.fangdao.common.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class HttpPathUtils {
    //assets中web资源目录
    private static final String assetsWebDir = "httpserver";
    //外部存储中web资源目录
    private static final String extWebDir = "00www";
    //外部存储中web资源绝对目录
    private static String extWebAbsPath;

    public static void initWebPath(Context context) {
        File rootFile = FileUtils.getRootFile(context);
        extWebAbsPath = rootFile.getAbsolutePath() + "/" + extWebDir;
        File file = new File(extWebAbsPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 读取资源文件
     * 优先从assets中读取，没有找到则从外部存储读取
     */
    public static InputStream readFile(Context context, String name) {
        InputStream fileByAssets = getFileByAssets(context, name);
        if (fileByAssets != null) {
            return fileByAssets;
        }

        return getFileByExt(context, name);
    }

    /**
     * 从assets中读取
     */
    public static InputStream getFileByAssets(Context context, String name) {
        try {
            //读写assets目录下的文件
            return context.getAssets().open(assetsWebDir + name);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 从外部存储读取
     */
    public static InputStream getFileByExt(Context context, String name) {
        File file = new File(extWebAbsPath + name);
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
