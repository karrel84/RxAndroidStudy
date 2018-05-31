package com.eastandroid.rxtest;

import android.content.Context;
import android.os.Bundle;

public class LFragment extends android.support.v4.app.Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.onActivityCreated(getClass(), savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.onAttach(getClass(), context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.onCreate(getClass(), savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.onDestroy(getClass());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.onDestroyView(getClass());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.onDetach(getClass());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.onPause(getClass());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.onResume(getClass());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.onStart(getClass());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.onStop(getClass());
    }

//    @Override
//    public void startActivity(Intent intent) {
//        super.startActivity(intent);
//        Log.startActivity(getClass(), intent);
//    }
//
//    @Override
//    public void startActivity(Intent intent, @Nullable Bundle options) {
//        super.startActivity(intent, options);
//        Log.startActivity(getClass(), intent, options);
//    }
//
//    @Override
//    public void startActivityForResult(Intent intent, int requestCode) {
//        super.startActivityForResult(intent, requestCode);
//        Log.startActivityForResult(getClass(), intent, requestCode);
//    }
//
//
//    @Override
//    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
//        super.startActivityForResult(intent, requestCode, options);
//        Log.startActivityForResult(getClass(), intent, requestCode, options);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.onActivityResult(getClass(), requestCode, resultCode, data);
//    }

}
