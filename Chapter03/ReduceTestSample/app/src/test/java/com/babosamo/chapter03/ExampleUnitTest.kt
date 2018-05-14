package com.babosamo.chapter03

import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun ball1() {
        val balls = listOf("a", "b", "c")
        val source: Maybe<String> = Observable.fromIterable(balls).reduce { value1, value2 -> "$value2($value1)" }
        source.subscribe { result ->
            assertEquals("c(b(a))", result)
            System.out.println(result)
        }
    }

    @Test
    fun ball2() {
        val balls = listOf<String>()
        val source: Maybe<String> = Observable.fromIterable(balls).reduce { value1, value2 -> "$value2($value1)" }
        source.subscribe { result ->
            assertEquals("c(b(a))", result)
            System.out.println(result)
        }
    }


    private val mergeBall: ((ball1: String, ball2: String) -> String) = { value1, value2 ->
        "$value2($value1)"
    }

    @Test
    fun ball3() {
        val arrayData = arrayOf("a", "b", "c")
        val source: Maybe<String> = Observable.fromIterable(arrayData.toList()).reduce(mergeBall)
        source.subscribe { result -> assertEquals("c(b(a))", result) }
    }


    @Test
    fun ball4() {
        val source: Maybe<String> = Observable.fromArray("a").reduce { value1, value2 -> value2 }
        source.subscribe { result -> assertEquals("a", result) }
    }


}
