package karrel.com.publishsubjectsample.rx

import io.reactivex.subjects.PublishSubject

/**
 * Created by Rell on 2018. 5. 7..
 */
object EventBus {

    private val publisher = PublishSubject.create<Long>()

    fun publish(event: Long) {
        publisher.onNext(event)
    }

    fun observable() = publisher

}