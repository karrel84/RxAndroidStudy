package com.babosamo.turnonoff

import android.util.Log
import android.widget.Button
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

object RoomLightManager {

    fun isTurnOn(room: Button, callback: (isOn: Boolean) -> Unit) {

        //api 를 호출하여 방의 on / off 상태를 가지고 온다.

        val isOn: Boolean = ((Math.random() * 3).toInt() > 0 )

        Thread({
            Thread.sleep(2000)
            Log.i(RoomLightManager.toString(), "${room.text} == $isOn")
            callback(isOn)
        }).start()
    }


    fun isTurnOn (room:Button): Observable<Pair<Button,Boolean>> {
        return Observable.zip(Observable.just(Pair(room, ((Math.random() * 3).toInt() > 0 ))), Observable.interval(2000, TimeUnit.MILLISECONDS), BiFunction{v:Pair<Button,Boolean>, a:Long -> v})
    }
}