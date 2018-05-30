package com.karrel.zipsample;


import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

/**
 * Created by kimmihye on 2018. 5. 30..
 */

public class ExampleUnitTestOnJava {
    public void getUserInf(){
        Observable.zip(Observable.just("1"), Observable.just("2"), new BiFunction<String, String, Object>() {
            @Override
            public Object apply(String s, String s2) throws Exception {
                return null;
            }
        });
    }
}
