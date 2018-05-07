package karrel.com.publishsubjectsample.rx

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Rell on 2018. 5. 7..
 */
class AutoClearOnDestroy(lifecycleOwner: AppCompatActivity) : AutoClearedDisposable(lifecycleOwner) {


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun detachSelf() {
        super.detachSelf()
    }
}