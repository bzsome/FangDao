package com.example.httpserver;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class MyServer extends NanoHTTPD {
    private Context mContext;
    private final static int PORT = 17123;
    public final static String TAG = "SharkChilli";

    public MyServer(Context context) throws IOException {
        super(PORT);
        this.mContext = context;
        start();
        Log.i(TAG, "MyServer start");
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

        try {
            String msg = "<html><body><h1>Hello server</h1></body></html>";
            return newFixedLengthResponse(msg);
        } catch (Exception exception) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Internal Server Error!!!");
        }
    }
}