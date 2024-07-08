package com.msinsight.config;

public class AppConfig {

    public static final String HOST = System.getenv("HOST");
    public static final String PORT = System.getenv("PORT");
    public static final String MYSQL_USER = System.getenv("MYSQL_USER");
    public static final String MYSQL_PASSWORD = System.getenv("MYSQL_PASSWORD");
    public static final String DATABASE = System.getenv("DATABASE");
    public static final String PUBLIC_KEY = System.getenv("PUBLIC_KEY");
    public static final String SECRET_KEY = System.getenv("SECRET_KEY");
    public static final String S3_FIXED_DATA = System.getenv("S3_FIXED_DATA");
    public static final String S3_VARIABLE_DATA = System.getenv("S3_VARIABLE_DATA");
    public static final String PATIENTS_CSV = System.getenv("PATIENTS");
    public static final String FORMS_CSV = System.getenv("FORMS");
    public static final String RESULTS_CSV = System.getenv("RESULTS");
    public static final String MEDICAL_CONSULTATIONS_CSV = System.getenv("MEDICAL");

}
