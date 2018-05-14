package karrel.com.calculatorreducesample

import io.reactivex.Observable
import org.junit.Test
import java.util.function.BiFunction
import java.util.regex.Pattern


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun cal() {
        val form = "20*10-50/80"

        // 패턴적용
        // "{숫자}", "(", ")", "+", "-", "*", "/"로 구분
        val numsPattern = Pattern.compile("[\\d]")
        val nonumPattern = Pattern.compile("[\\D]")

        val nonumArray = numsPattern.split(form)
        val numsArray = nonumPattern.split(form)
        for (s in nonumArray) {
            println("nonumArray : ${s}")
        }
        for (s in numsArray) {
            println("numsArray : ${s}")
        }


    }

    @Test
    fun cal2() {
        val form = "2*1-5/8"
        val numberPattern = Pattern.compile("\\d")

        for (s in numberPattern.split(form)) {
            println(s)
        }

        val matcher = numberPattern.matcher(form)
        while (matcher.find()) {
            println(matcher.group())

        }
    }

    @Test
    fun streamTest() {
        val x = arrayOf("a", "b", "c")
        val streamA = Observable.fromIterable(x.toList())

        streamA.subscribe {
            println(it)
        }
    }

    @Test
    fun cal3() {
        val form = "2*1-5/8"
        val numberPattern = Pattern.compile("\\d")
        val signPattern = Pattern.compile("\\D")

        val signArray = numberPattern.split(form).toList()
        val numsArray = signPattern.split(form).toList()

        val signObervable = Observable.fromIterable(signArray)
                .filter({ v -> v.length != 0 })
        val numsObservable = Observable.fromIterable(numsArray)

        signObervable.subscribe { println(it) }
        numsObservable.subscribe { println(it) }

//        signObervable.zip

    }


    @Test
    fun cal4() {
        val form = "2*1-5/8"

        val valueObervable = Observable.fromIterable(form.toCharArray().toList())
//        valueObervable.reduce { t1: Char, t2: Char ->
//
//        }
        valueObervable.subscribe({ println(it) })

//        signObervable.zip

    }
}
