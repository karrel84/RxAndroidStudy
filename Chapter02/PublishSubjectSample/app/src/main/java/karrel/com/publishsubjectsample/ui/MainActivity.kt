package karrel.com.publishsubjectsample.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import karrel.com.publishsubjectsample.R
import karrel.com.publishsubjectsample.extensions.plusAssign
import karrel.com.publishsubjectsample.rx.AutoClearOnDestroy
import karrel.com.publishsubjectsample.rx.EventBus
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor
import java.util.concurrent.TimeUnit

/**
 * PublishSubject를 이용한 이벤트버스를 테스트하기위한 클래스이다
 * 액티비티가 실행될때 이벤트버스로 보낼 이벤트를 발생한다
 */
class MainActivity : AppCompatActivity() {

    private val disposablesOnDestroy = AutoClearOnDestroy(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle += disposablesOnDestroy // add lifecycle

        setupEvent() // button event

        publishEvent()
        receiveEvent()
    }

    private fun setupEvent() {
        // intentFor 는 Anko
        aActivity.setOnClickListener { startActivity(intentFor<AActivity>()) }
        bActivity.setOnClickListener { startActivity(intentFor<BActivity>()) }
    }

    private fun receiveEvent() {
        disposablesOnDestroy += EventBus.observable().subscribe {
            log.append("${it} ")
        }
    }

    private fun publishEvent() {
        disposablesOnDestroy += timer.subscribe {
            println("publish event ${it}")
            EventBus.publish(it)
        }
    }

    private val timer: Observable<Long> = Observable.interval(1, TimeUnit.SECONDS)
}
