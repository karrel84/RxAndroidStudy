package karrel.com.switchmapsample.etc

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.Jsoup
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by Rell on 2018. 5. 21..
 */
class GoogleImageScrapperKtTest {

    @Test
    fun getImageUrlsFromKeyword() {
        getImageUrlsFromKeyword("안녕")
                ?.filter { it.endsWith(".jpg") or it.endsWith(".png") or it.endsWith(".gif") }
                ?.take(10)
                ?.subscribe {
                    println("url : $it")
                }
    }

    @Test
    fun getImageUrlsFromKeyword2() {
        val url = String.format(baseUrl, "안녕")
        Observable.just(url)
                .subscribeOn(Schedulers.io()) // io 스케쥴러
                .map { Jsoup.connect(it).get() } // get Document from url
                .map { it.getElementsByClass("rg_meta notranslate") } // get Elements from class name
                .flatMap { Observable.fromIterable(it.asIterable()) } // flat iterable
                .map { it.html() } // Element to html
                .map { JSONObject(it).getString("ou") } // get value from json
                .filter { it.endsWith(".jpg") or it.endsWith(".png") } // get only jpg or png
                .observeOn(AndroidSchedulers.mainThread())
    }
}