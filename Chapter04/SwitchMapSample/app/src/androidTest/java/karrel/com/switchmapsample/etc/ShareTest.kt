package karrel.com.switchmapsample.etc

import io.reactivex.Observable
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Created by Rell on 2018. 5. 31..
 */

class ShareTest {


    @Test
    fun shareTest() {
        val shareObservable = Observable.just("hello")
                .delay(1000, TimeUnit.MILLISECONDS)
                .share()
                .doOnNext { println(it) }


        shareObservable.map { "share1" }.subscribe { println(it) }
        shareObservable.map { "share2" }.subscribe { println(it) }
        shareObservable.map { "share3" }.subscribe { println(it) }

        println("share test")

        Thread.sleep(2000)
    }
}