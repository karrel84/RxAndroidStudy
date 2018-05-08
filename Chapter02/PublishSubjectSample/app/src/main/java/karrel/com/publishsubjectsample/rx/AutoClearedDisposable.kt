package karrel.com.publishsubjectsample.rx

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Rell on 2018. 5. 7..
 *
 * 라이프사이클의 상태변화에 따라 구독중인 Observable 을 해제하기 위한 클래스
 *
 */
open class AutoClearedDisposable(
        private val lifecycleOwner: AppCompatActivity,
        private val compositeDisposable: CompositeDisposable = CompositeDisposable())
    : LifecycleObserver {

    fun add(disposable: Disposable) {
        // false 이면 illegalStateException 던진다
        check(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED))
        compositeDisposable.add(disposable)
    }


    open fun detachSelf() {
        compositeDisposable.clear()
        lifecycleOwner.lifecycle.removeObserver(this)

    }
}