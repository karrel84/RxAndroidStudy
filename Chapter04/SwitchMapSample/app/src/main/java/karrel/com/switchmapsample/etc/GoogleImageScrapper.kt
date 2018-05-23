package karrel.com.switchmapsample.etc

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Created by Rell on 2018. 5. 18..
 */

//val baseUrl = "https://www.google.co.kr/search?q=%s&source=lnms&tbm=isch&sa=X&ved=0ahUKEwipxPTktY7bAhVEvbwKHdITAFMQ_AUICigB&biw=1234&bih=960"
//val baseUrl = "https://www.google.com/search?q=%s&prmd=ivmn&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiYnsXPxpXbAhXJabwKHU8GBQYQ_AUIESgB"
val baseUrl = "https://www.google.co.kr/search?q=%s&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiZlpWi45XbAhWDgrwKHUzxAVUQ_AUICigB&biw=480&bih=480"

fun getImageUrlsFromKeyword(keyword: String): Observable<String>? {
    val url = String.format(baseUrl, keyword)
    return Observable.just(url)
            .subscribeOn(Schedulers.io()) // io 스케쥴러
            .map { Jsoup.connect(it).get() } // get Document from url
            .map { it.getElementsByClass("rg_meta notranslate") } // get Elements from class name
            .flatMap { Observable.fromIterable(it.asIterable()) } // flat iterable
            .map { it.html() } // Element to html
            .map { JSONObject(it).getString("ou") } // get value from json
            .filter { it.endsWith(".jpg") or it.endsWith(".png") } // get only jpg or png
            .observeOn(AndroidSchedulers.mainThread())
}
