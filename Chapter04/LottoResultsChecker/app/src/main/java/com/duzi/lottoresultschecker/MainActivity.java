package com.duzi.lottoresultschecker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=";
    private static final int ROUND = 808;

    private TextView lottoNumber, lottery1, lottery2, lottery3, lottery4, lottery5;
    private Button btnMakeNum;

    private ArrayList<Integer> randomNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutInit();
        setDefaultSettings();
    }

    private void layoutInit() {
        lottoNumber = (TextView) findViewById(R.id.tv_lotto_result_number);
        lottery1 = (TextView) findViewById(R.id.tv_lottery1);
        lottery2 = (TextView) findViewById(R.id.tv_lottery2);
        lottery3 = (TextView) findViewById(R.id.tv_lottery3);
        lottery4 = (TextView) findViewById(R.id.tv_lottery4);
        lottery5 = (TextView) findViewById(R.id.tv_lottery5);

        btnMakeNum = (Button) findViewById(R.id.btn_make_num);
        btnMakeNum.setOnClickListener(v -> makeAndCheck());
    }

    private void setDefaultSettings() {
        randomNumbers = new ArrayList<>();
        for(int i = 1; i <= 45; i++) {
            randomNumbers.add(i);
        }
    }

    private void makeAndCheck() {
        Observable<String> jsonSource = Observable.just(URL + ROUND)
                .map(OkHttpHelper::response)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share();

        Observable<Lotto> resultNumber = jsonSource.map(this::parseLottoNumber);
        resultNumber.subscribe(lotto -> lottoNumber.setText(lotto.getNumberWithBonus()));

        Observable<Lotto> randomNumbers = Observable.range(0, 5).map(notUsed -> makeLottoNumber());

        // ------------------------------------------json---------|
        // ------1---2---3---4---5--------------------------------|
        // output : (json, 5)

        // --json-------------------------------------------------|
        // ------------1---2---3---4---5--------------------------|
        // output : (json, 1) (json, 2) (json, 3) (json, 4) (json, 5)

        // test 1
        /*Observable<Pair<Lotto, String>> resultSource = Observable.zip(
                resultNumber.flatMap(l -> Observable.range(0,5).map(notUsed -> l)),
                randomNumbers,
                (a, b) -> checkLottoResult(a,b));*/

        // test 2
        /*Observable<Pair<Lotto, String>> resultSource = Observable.combineLatest(
                resultNumber,
                randomNumbers,
                (a, b) -> checkLottoResult(a,b));*/

        // test 3
        Observable<Pair<Lotto, String>> resultSource =
                resultNumber.concatMap(result -> randomNumbers.map(random -> checkLottoResult(result, random)));

        // Pair<Pair<Lotto, Sting>, TextView>
        TextView[] lists = {lottery1, lottery2, lottery3, lottery4, lottery5};
        Observable.zip(resultSource, Observable.fromArray(lists), (result, tv) -> Pair.create(result, tv))
                .doOnNext(notUsed -> Log.d("MainActivity", Thread.currentThread().getName() + " | MainActivity.check"))
                .subscribe(f -> f.second.setText(f.first.first.getNumber() + "\t\t당첨 결과 : " + f.first.second));
    }

    private Lotto parseLottoNumber(String json) {
        Gson gson = new GsonBuilder().create();
        Lotto lotto = gson.fromJson(json, Lotto.class);
        return lotto;
    }

    private Lotto makeLottoNumber() {
        Lotto lotto = new Lotto();
        ArrayList<Integer> genaratedNumbers = new ArrayList<>();

        // 랜덤 섞기
        Collections.shuffle(randomNumbers);
        for(int i = 0; i < 6; i++) {
            genaratedNumbers.add(randomNumbers.get(i));
        }

        // 오름차순 정렬
        Collections.sort(genaratedNumbers, (o1, o2) -> o1.compareTo(o2));
        lotto.drwtNo1 = String.valueOf(genaratedNumbers.get(0));
        lotto.drwtNo2 = String.valueOf(genaratedNumbers.get(1));
        lotto.drwtNo3 = String.valueOf(genaratedNumbers.get(2));
        lotto.drwtNo4 = String.valueOf(genaratedNumbers.get(3));
        lotto.drwtNo5 = String.valueOf(genaratedNumbers.get(4));
        lotto.drwtNo6 = String.valueOf(genaratedNumbers.get(5));

        return lotto;
    }

    private Pair<Lotto, String> checkLottoResult(Lotto result, Lotto random) {
        boolean bonus = false;
        String[] results = { result.drwtNo1, result.drwtNo2, result.drwtNo3,
                result.drwtNo4, result.drwtNo5, result.drwtNo6, result.bnusNo};

        String[] randoms = { random.drwtNo1, random.drwtNo2, random.drwtNo3,
                random.drwtNo4, random.drwtNo5, random.drwtNo6};

        int count = 0;
        for(String r : randoms) {
            if(r.equals(results[6])) bonus = true;
            if(Arrays.asList(results).contains(r)) count++;
        }

        String winResult = "";
        if(bonus && count == 5) winResult = "2등당첨";
        switch(count) {
            case 6: winResult = "1등당첨"; break;
            case 5: winResult = "3등당첨"; break;
            case 4: winResult = "4등당첨"; break;
            case 3: winResult = "5등당첨"; break;
            case 0:
            case 1:
            case 2: winResult = "꽝"; break;
        }

        return Pair.create(random, winResult);
    }
}
