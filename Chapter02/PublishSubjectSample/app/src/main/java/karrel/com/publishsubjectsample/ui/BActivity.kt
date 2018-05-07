package karrel.com.publishsubjectsample.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import karrel.com.publishsubjectsample.R
import karrel.com.publishsubjectsample.extensions.plusAssign
import karrel.com.publishsubjectsample.rx.AutoClearOnDestroy
import karrel.com.publishsubjectsample.rx.EventBus
import kotlinx.android.synthetic.main.activity_b.*

class BActivity : AppCompatActivity() {

    private val disposables = AutoClearOnDestroy(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)

        lifecycle += disposables
        recieveEvent()
    }

    private fun recieveEvent() {
        disposables += EventBus.observable().subscribe {
            log.append("${it} ")
        }
    }
}
