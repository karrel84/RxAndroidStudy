package com.karrel.zipsample

import io.reactivex.Observable
import io.reactivex.schedulers.Timed
import org.junit.Assert
import org.junit.Test

/**
 * zip 함수의 예제로
 * 3개의 url을 호출하여 정보를 조합한다고 가정한다.
 * 이경우 zip 을 이용하면 어떤장점이 있는지 확인해본다.
 */
class ExampleUnitTest {
    // 3가지 문제
    // 1. 멤버변수 사용으로 인한 순수함수 파괴, 경쟁조건 발생시 부수효과 발생
    // 2. 콜백 지옥
    // 3. 불필요한 시간 소비
    var userName: String? = null
    var userJob: String? = null
    var userAge: String? = null
    var intervalTime: Long = 0
    @Test
    fun getUserInfoRegacy() {
        CommonUtils.exampleStart()
        var userId = "rell"
        getUserAge(userId)?.timeInterval()?.subscribe {
            userAge = it.value()
            intervalTime = it.time()
            getUserName(userId)?.timeInterval()?.subscribe {
                userName = it.value()
                intervalTime += it.time()
                getUserJob(userId)?.timeInterval()?.subscribe {
                    userJob = it.value()
                    intervalTime += it.time()
                    val result = "$userName ( ${userAge}세, ${userJob})"
                    println("${intervalTime} : $result")
                    assertUserInfo(result, intervalTime)
                }
            }
        }
        CommonUtils.sleep(10000)
    }

    @Test
    fun getUserInfoFromZip() {
        CommonUtils.exampleStart()
        val userId = "rell"
        val biFunction = io.reactivex.functions.Function3<String, String, String, String> { age, job, name ->
            "$name ( ${age}세, $job)"
        }
        val getUserInfo = Observable.zip(getUserAge(userId), getUserJob(userId), getUserName(userId), biFunction)

        getUserInfo
                .timeInterval()
                .subscribe {
                    println("${it.time()} : ${it.value()}")
                    assertUserInfo(it.value(), it.time())
                }

        CommonUtils.sleep(5000)
    }

    fun assertUserInfo(userInfo: String, time: Long) {
        Assert.assertEquals(userInfo, "이주영 ( 35세, 개발자)")
        Assert.assertFalse(time > 3000)
    }
}
