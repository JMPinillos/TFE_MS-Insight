package com.msinsight.utils;

public class ReadAnswersValuesUtil {

    private String title;
    private double value;

    public ReadAnswersValuesUtil(String title, double value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public double getValue() {
        return value;
    }
}
