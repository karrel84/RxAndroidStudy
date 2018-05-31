package com.eastandroid.rxtest;

public class BU {
    public static String getNumberText(String numbar) {

        String[] unit4 = new String[]{"", "십", "백", "천"};
        String[] unit = new String[]{"", "만", "억", "조", "경", "해", "자", "양", "구", "간", "정", "재", "극", "항하사", "아승기", "나유타", "불가사의", "무량대수"};
        String[] number = new String[]{"", "", "이", "삼", "사", "오", "육", "칠", "팔", "구"};

        String numbar_removed = numbar.replaceAll("\\D", "");
        if (numbar_removed.length() == 1 && numbar_removed.charAt(0) == '0')
            return "영";

        StringBuilder text = new StringBuilder(numbar_removed);
//        text.reverse();
        int N = text.length();

        int numSum = 0;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < N; i++) {
            char ch = text.charAt(i);
            int po = N - i - 1;
            int num = ch - '0';

            numSum += num;
            result.append(((po == 0 && num == 1) ? "일" : number[num])//
                    + ((num > 0) ? unit4[po % 4] : "") //
                    + ((numSum > 0 && po % 4 == 0) ? unit[po / 4] + "" : "")//
            );

            if (po % 4 == 0)
                numSum = 0;
        }
        return result.toString();
    }


}
