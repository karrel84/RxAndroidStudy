package com.duzi.lottoresultschecker;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by KIM on 2018-05-29.
 */

public class OkHttpHelper {
    private static final OkHttpClient client = new OkHttpClient();

    public static String response(String url) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response res = client.newCall(request).execute();
            Log.d("OKHttpHelper", Thread.currentThread().getName() + " | OkHttpHelper.response");
            return res.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
