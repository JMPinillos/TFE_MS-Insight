package com.msinsight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MySqlFormsResultsText {
    private int form_id;
    private int question_id;
    private int answer_id;
    private int patient_form_id;
    private int result_id;
    private String value;
}
