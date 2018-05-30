package com.babosamo.turnonoff

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableAll
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        requestRoomLightStateCallBack()
        rxZipAndAll()
    }

    private fun requestRoomLightStateCallBack() {

        RoomLightManager.isTurnOn(room_1_button, { isOn ->
            room_1_button.setTextColor(getRoomColor(isOn))
        })

        RoomLightManager.isTurnOn(room_2_button, { isOn ->
            room_2_button.setTextColor(getRoomColor(isOn))
        })

        RoomLightManager.isTurnOn(room_3_button, { isOn ->
            room_3_button.setTextColor(getRoomColor(isOn))
        })

        RoomLightManager.isTurnOn(room_4_button, { isOn ->
            room_4_button.setTextColor(getRoomColor(isOn))
        })

        // how to check... is On or not

    }


    private fun rxZipAndAll() {

        val list = listOf<Button>(room_1_button, room_2_button, room_3_button, room_4_button)

        val src: Observable<Pair<Button, Boolean>> = Observable.fromIterable(list).flatMap { it -> RoomLightManager.isTurnOn(it) }.doOnNext { Log.i(MainActivity::class.java.simpleName, "${it.first.text} :: ${it.second}") }
                .share()

        src.subscribe({
            it.first.setTextColor(getRoomColor(it.second))
        })

        src.all({ it -> it.second }).subscribe({ it ->
            all_button.setTextColor(getRoomColor(it))
            Log.i(MainActivity::class.java.simpleName, "${all_button.text} :: ${it}")
        })
    }


    private val onColor by lazy { Color.MAGENTA }

    private val offColor by lazy { Color.BLACK }

    private fun getRoomColor(isOn: Boolean) = if (isOn) onColor else offColor


}
