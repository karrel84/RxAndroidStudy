package com.duzi.lottoresultschecker;

/**
 * Created by KIM on 2018-05-29.
 */

public class Lotto {
    public String bnusNo;
    public String firstAccumamnt;
    public String firstWinamnt;
    public String returnValue;
    public String totSellamnt;
    public String drwtNo1;
    public String drwtNo2;
    public String drwtNo3;
    public String drwtNo4;
    public String drwtNo5;
    public String drwtNo6;
    public String drwNoDate;
    public String drwNo;
    public String firstPrzwnerCo;

    public String getNumber() {
        return drwtNo1 + " " + drwtNo2 + " " + drwtNo3 + " " + drwtNo4 + " " + drwtNo5 + " " + drwtNo6;
    }

    public String getNumberWithBonus() {
        return drwtNo1 + " " + drwtNo2 + " " + drwtNo3 + " " + drwtNo4 + " " + drwtNo5 + " " + drwtNo6 + " " + bnusNo;
    }
}
