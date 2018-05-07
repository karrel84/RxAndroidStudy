package karrel.com.publishsubjectsample.extensions

import io.reactivex.disposables.Disposable
import karrel.com.publishsubjectsample.rx.AutoClearedDisposable

operator fun AutoClearedDisposable.plusAssign(disposable: Disposable) = this.add(disposable)
