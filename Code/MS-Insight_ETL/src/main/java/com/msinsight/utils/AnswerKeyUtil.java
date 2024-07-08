package com.msinsight.utils;

public class AnswerKeyUtil {
    // MÉTODO PARA GENERAR CLAVE ÚNICA PARA LAS RESPUESTAS
    public static String generateAnswerKey(String title, String type, double value) {
        return title + "|" + type + "|" + (Double.isNaN(value) ? "NaN" : value);
    }
}
