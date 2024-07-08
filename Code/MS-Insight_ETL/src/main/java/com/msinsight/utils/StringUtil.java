package com.msinsight.utils;

import java.text.Normalizer;

public class StringUtil {

    public static String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", "")
                .replaceAll("\\.", "");
    }
}
