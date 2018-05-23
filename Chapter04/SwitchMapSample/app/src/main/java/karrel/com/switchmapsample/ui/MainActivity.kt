package karrel.com.switchmapsample.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import karrel.com.switchmapsample.R
import karrel.com.switchmapsample.etc.getImageUrlsFromKeyword
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val eventDelayTime = 300L

    private lateinit var imageAdapter: ImageAdapter
    private val gridLayoutManager = GridLayoutManager(this@MainActivity, 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()

        setupSearchEvent() // 이미지 로딩

    }

    private fun setupSearchEvent() {
        inputChangeEvent()
                .doOnNext { imageAdapter.clearImages() }
                .map { keyword -> keyword.toString() }
                .flatMap(::getImageUrlsFromKeyword) //구글 이미지 스크랩퍼에서 이미지 url을 가져온다
                .take(100)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ imageAdapter.addImageUrl(it) }) {
                    it.printStackTrace()
                }
    }

    private fun setupRecyclerView() {
        imageAdapter = ImageAdapter(this@MainActivity)
        with(recyclerView) {
            layoutManager = gridLayoutManager
            adapter = this@MainActivity.imageAdapter
        }
    }

    private fun inputChangeEvent(): Observable<CharSequence> {
        return RxTextView.textChanges(input)
                .filter { it.isNotEmpty() } // 값이 있어야한다
                .switchMap { msg -> Observable.timer(eventDelayTime, TimeUnit.MILLISECONDS).map { msg } } // 특정 초 후에 1번만 이벤트를 발행
    }

}
