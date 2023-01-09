package com.bzchao.fangdao.httpserver;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class MyHttpServer2 extends NanoHTTPD {
    public final static String TAG = "MyHttpServer";
    public static Context context;

    public MyHttpServer2(Context context, int port) {
        super(port);
        MyHttpServer2.context = context;
        HttpPathUtils.initWebPath(context);
    }

    @Override
    public Response serve(IHTTPSession session) {
        //打印请求数据
        Log.i(TAG, "serve uri: " + session.getUri());
        Log.i(TAG, "serve getQueryParameterString: " + session.getQueryParameterString());
        Log.i(TAG, "serve getRemoteHostName: " + session.getRemoteHostName());
        Log.i(TAG, "serve getRemoteIpAddress: " + session.getRemoteIpAddress());

        for (Map.Entry entry : session.getHeaders().entrySet()) {
            Log.i(TAG, entry.getKey() + " : " + entry.getValue());
        }

        InputStream inputStream = session.getInputStream();

        InputStream fileIn = HttpPathUtils.readFile(context, session.getUri());
        if (fileIn != null) {
            return render200(session.getUri(), fileIn);
        }
        if (session.getUri().endsWith("/")) {
            String newUri = session.getUri() + "index.html";
            return render301(newUri);
        }
        return render404();
    }

    private Response render404() {
        InputStream fileIn = HttpPathUtils.readFile(context, "404.html");
        if (fileIn == null) {
            return NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_HTML, null);

        }
        try {
            return NanoHTTPD.newChunkedResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, fileIn);
        } catch (Exception e) {
            return render500(e.getMessage());
        }
    }

    private Response render500(String text) {
        return NanoHTTPD.newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, text);
    }

    private Response render200(String uri, InputStream inputStream) {
        try {
            return NanoHTTPD.newChunkedResponse(Response.Status.OK, NanoHTTPD.getMimeTypeForFile(uri), inputStream);
        } catch (Exception e) {
            return render500(e.getMessage());
        }
    }

    private Response render301(String next) {
        Response res = newFixedLengthResponse(Response.Status.REDIRECT, NanoHTTPD.MIME_HTML, null);
        res.addHeader("Location", next);
        return res;
    }

}