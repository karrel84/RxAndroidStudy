package com.karrel.zipsample

import com.karrel.zipsample.extensions.random
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun getUserAge(userId: String): Observable<String>? {
    return Observable.timer((100..3000).random().toLong(), TimeUnit.MILLISECONDS)
            .map { "35" }
            .doOnNext { println("${CommonUtils.checkInteval()} : responded user's age") }
}