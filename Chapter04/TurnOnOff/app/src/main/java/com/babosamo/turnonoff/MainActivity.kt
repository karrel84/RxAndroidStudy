package com.babosamo.turnonoff

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestRoomLightStateCallBack()
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


    private fun rxZipAndAll(){

    }


    private val onColor by lazy { Color.MAGENTA }

    private val offColor by lazy { Color.BLACK }

    private fun getRoomColor(isOn: Boolean) = if (isOn) onColor else offColor


}
