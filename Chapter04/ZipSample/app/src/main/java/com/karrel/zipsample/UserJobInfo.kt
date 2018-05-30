package com.karrel.zipsample

import com.karrel.zipsample.extensions.random
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


/**
 * Created by kimmihye on 2018. 5. 30..
 */
fun getUserJob(userId:String): Observable<String>? {
    return Observable.timer((100..3000).random().toLong(), TimeUnit.MILLISECONDS)
            .map { "개발자" }
            .doOnNext { println("${CommonUtils.checkInteval()} : responded user's job") }
}
