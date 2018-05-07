package karrel.com.publishsubjectsample.rx

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Rell on 2018. 5. 7..
 */
open class AutoClearedDisposable(
        private val lifecycleOwner: AppCompatActivity,
        private val compositeDisposable: CompositeDisposable = CompositeDisposable())
    : LifecycleObserver {

    fun add(disposable: Disposable) {
        // false 이면 EllegalStateException을 던진다
        check(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED))
        compositeDisposable.add(disposable)
    }


    open fun detachSelf() {
        compositeDisposable.clear()
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}