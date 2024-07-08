package com.msinsight.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class DataTypeDistinguisherUtil {

    // Define los patrones de expresiones regulares para detectar números y fechas
    private static final Pattern numberPattern = Pattern.compile("-?\\d+(\\.\\d+)?"); // Números con decimales opcionales
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Define el formato de fecha esperado

    static {
        dateFormat.setLenient(false); // Configura el parseador para ser estricto con el formato de fecha
    }

    public static String distinguishDataType(String data) {
        // Verifica si es un número (Double)
        if (numberPattern.matcher(data).matches()) {
            return "Double";
        }

        // Intenta parsear como una fecha
        try {
            dateFormat.parse(data);
            return "Date"; // Si se parsea correctamente, se trata de una fecha
        } catch (ParseException e) {
            // No es una fecha válida
        }

        // Por defecto, trata el dato como String
        return "String";
    }
}
