package karrel.com.publishsubjectsample.rx

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.app.AppCompatActivity

/**
 * Created by Rell on 2018. 5. 7..
 *
 * 라이프사이클 onStop이 호출되면 구독을 해제한다
 */
class AutoClearOnStop(lifecycleOwner: AppCompatActivity) : AutoClearedDisposable(lifecycleOwner) {


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun detachSelf() {
        super.detachSelf()
    }
}