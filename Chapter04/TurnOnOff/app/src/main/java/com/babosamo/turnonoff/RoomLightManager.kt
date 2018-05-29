package com.babosamo.turnonoff

import android.util.Log
import android.widget.Button

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
}