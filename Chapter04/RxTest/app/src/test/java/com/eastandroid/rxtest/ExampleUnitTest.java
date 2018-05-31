package com.eastandroid.rxtest;

import android.util.Pair;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void toKorean() {
        String[] unit4 = new String[]{"", "십", "백", "천"};
        String[] unit = new String[]{"", "만", "억", "조", "경", "해", "자", "양", "구", "간", "정", "재", "극", "항하사", "아승기", "나유타", "불가사의", "무량대수"};
        String[] num = new String[]{"영", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};

        String number = "12345678901";
        char[] number_array = number.toCharArray();

//        Observable.fromArray(new String[]{"1", "2"})//
//                .doOnNext(s -> System.out.println(Log._MESSAGE("next : " + s)))//
//                .subscribe(chars -> System.out.println(Log._MESSAGE(chars)))//
//        ;

//        Observable.fromIterable(Arrays.asList(number_array))//
//                .subscribe(chars -> System.out.println(Log._MESSAGE(chars)))//
//        ;

//        Observable.just(number.split(""))//
//                .subscribe(chars -> System.out.println(Log._MESSAGE(chars)))//
//        ;

        Observable.just(number)//
                .flatMap(s -> Observable.fromArray(s.split("")))//
                .map(Integer::parseInt)//
                .skipWhile(s -> s == 0)//
                .map(i -> num[i])//
                .reduce((l, r) -> l + r)//
                .subscribe(chars -> System.out.println(Log._MESSAGE(chars)))//
        ;

        Observable.range(0, number.length())//
                .map(i -> number.length() - i - 1)//
                .map(i -> i % 4 == 0 ? unit[i / 4] : unit4[i % 4])//
                .reduce((l, r) -> l + r)//
                .subscribe(integer -> System.out.println(Log._MESSAGE(integer)))//
        ;

        Observable.zip(Observable.just(number)//
                        .flatMap(s -> Observable.fromArray(s.split("")))//
                        .map(Integer::parseInt)//
                        .skipWhile(s -> s == 0)//
                        .map(i -> num[i])//
                , Observable.range(0, number.length())//
                        .map(i -> number.length() - i - 1)//
                        .map(i -> i % 4 == 0 ? unit[i / 4] : unit4[i % 4])//
                , (f, s) -> f + s)//
                .reduce((l, r) -> l + r)//
                .subscribe(integer -> System.out.println(Log._MESSAGE(integer)))//
        ;

        assertEquals("백이십삼억사천오백육십칠만팔천구백일", BU.getNumberText(number));
    }
}