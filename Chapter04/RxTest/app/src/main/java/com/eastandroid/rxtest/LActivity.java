package com.eastandroid.rxtest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class LActivity extends android.support.v7.app.AppCompatActivity {
    @Override
    public void sendBroadcast(Intent intent) {
        Log.sendBroadcast(getClass(), intent);
        super.sendBroadcast(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        Log.onCreate(getClass());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.onNewIntent(getClass());
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        Log.onDestroy(getClass());
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.onStart(getClass());
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.onStop(getClass());
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.onRestart(getClass());
        super.onRestart();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void startActivities(Intent[] intents) {
        Log.startActivities(getClass(), intents);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            try {
                intents[1].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent.getActivities(this, 0, intents, PendingIntent.FLAG_ONE_SHOT).send();
            } catch (CanceledException e) {
                e.printStackTrace();
            }
        } else
            super.startActivities(intents);
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            Log.startActivityForResult(getClass(), intent, requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    @SuppressLint("RestrictedApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        Log.startActivityForResult(getClass(), intent, requestCode, options);
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode >> 16) == 0)
            Log.onActivityResult(getClass(), requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }
}
