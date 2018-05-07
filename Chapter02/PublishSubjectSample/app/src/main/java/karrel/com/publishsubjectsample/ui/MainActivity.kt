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

    private val disposablesOnDestory = AutoClearOnDestroy(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle += disposablesOnDestory // add lifecycle

        setupEvent() // button event

        publishEvent()
        recieveEvent()
    }

    private fun setupEvent() {
        // intentFor 는 Anko
        aActivity.setOnClickListener { startActivity(intentFor<AActivity>()) }
        bActivity.setOnClickListener { startActivity(intentFor<BActivity>()) }
    }

    private fun recieveEvent() {
        disposablesOnDestory += EventBus.observable().subscribe {
            log.append("${it} ")
        }
    }

    private fun publishEvent() {
        disposablesOnDestory += timmer.subscribe {
            println("publish event ${it}")
            EventBus.publish(it)
        }
    }

    private val timmer: Observable<Long> = Observable.interval(1, TimeUnit.SECONDS)
}
