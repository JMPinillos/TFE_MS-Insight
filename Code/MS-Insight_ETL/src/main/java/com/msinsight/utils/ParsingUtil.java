package com.msinsight.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingUtil {

    public static ReadAnswersValuesUtil parseAnswer(String answerValue) {
        Pattern pattern = Pattern.compile("(.+) \\((\\d+(,\\d+)?)\\)");
        Matcher matcher = pattern.matcher(answerValue);

        if (matcher.matches()) {
            String title = matcher.group(1).trim();
            double value = Double.parseDouble(matcher.group(2).replace(",", "."));
            return new ReadAnswersValuesUtil(title, value);
        } else {
            return new ReadAnswersValuesUtil(answerValue, Double.NaN);
        }
    }

}
