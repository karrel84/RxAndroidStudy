package karrel.com.publishsubjectsample.rx

import io.reactivex.subjects.PublishSubject

/**
 * Created by Rell on 2018. 5. 7..
 *
 * RxJava를 이용한 싱글턴 이벤트 버스 클래스
 */
object EventBus {

    private val publisher = PublishSubject.create<Long>()

    fun publish(event: Long) {
        publisher.onNext(event)
    }

    fun observable() = publisher

}